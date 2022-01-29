zcacheSource = function() {
	let me = this;
	let dbName = "static_resource_zq"; //数据库名称
	me.db = null;
	me.dbName = dbName;
	me.openRequest = null;
	me.isCacheReady = false;
}
//zcacheSource类
zcacheSource.prototype = {
	constructor: zcacheSource, //构造器

	/**
	 * 初始化方法
	 */
	init: function(src,callback) {
		let me = this;
		let db = me.db;
		let dbName = me.dbName;
		let openRequest = me.openRequest = window.indexedDB.open(dbName, 1);
		if(Object.prototype.toString.call(callback).replace(/^\[object (.+)\]$/, '$1').toLowerCase()!="function"){
			callback=function(){
				window.location.reload();
			};
		}
		openRequest.onupgradeneeded = function(e) {
			me.db = e.target.result;
			if (!me.db.objectStoreNames.contains(dbName)) {
				let objectStore = me.db.createObjectStore(dbName, {
					keyPath: 'resourceEncodeName'
				});
				objectStore.createIndex('resourceEncodeName', 'resourceEncodeIndex', {
					unique: false
				});
			}
		}
		openRequest.onsuccess = function(e) {
			console.log("启动成功！");
			me.isCacheReady = true;
			me.db = openRequest.result;
			me.getResource("../static/json/jsResourceCache.json",function(data){
				if(Object.prototype.toString.call(src).replace(/^\[object (.+)\]$/, '$1').toLowerCase()=="array"){
					data.resource.forEach(function (jsPath,index) {
						switch(jsPath.action){
							case "add":
								me.addCache(jsPath.url);
								break;
							case "remove":
								me.removeCache(jsPath.url);
								break;
							case "update":
								me.updateCache(jsPath.url);
								break;
							default:
								console.log("jsResourceCache文件有未知行为")
								break;
						}
					});
					for(let i=0;i<src.length;i++){
						if(i==src.length-1) {
							me.queryCache(src[i],callback);
						}else{
							me.queryCache(src[i]);
						}
					}
				}else{
					//一个js文件暂不考虑
					me.queryCache(src,callback);
				}
				console.log(data);//更新策略
			});
		}

		openRequest.onerror = function(e) {
			console.error("启动失败！")
			me.isCacheReady = false;
		}
	},

	/**
	 * 添加资源到缓存里
	 * @param {Object} data
	 */
	addCache: function(data) {
		let me = this;
		let db = me.db;
		let dbName = me.dbName;
		var request = db.transaction([dbName], 'readwrite')
			.objectStore(dbName)
			.add(data);
		request.onsuccess = function(event) {
			console.log('资源写入成功');
		};
		request.onerror = function(event) {
			console.log('资源写入失败');
		}
	},

	/**
	 * 通过base64转换的资源路径移除缓存的资源
	 * @param {Object} data 
	 */
	removeCache: function(data) {
		let me = this;
		let db = me.db;
		let dbName = me.dbName;
		let request = db.transaction([dbName], 'readwrite')
			.objectStore(dbName)
			.delete(data);
		request.onsuccess = function(event) {
			console.log('资源删除成功');
		};
	},

	/**
	 * 通过base64转换的资源路径更新缓存的资源
	 * 调用该方法会重新请求资源
	 * @param {Object} src
	 */
	updateCache: function(src) {
		let me = this;
		let db = me.db;
		let dbName = me.dbName;
		let enSrc = window.btoa(src);
		let xhr = new XMLHttpRequest();
		xhr.open('GET', src, true);
		xhr.responseType = 'blob';
		xhr.onload = function() {
			let blob = new Blob([xhr.response]);
			let request = db.transaction([dbName], 'readwrite')
				.objectStore(dbName)
				.put({
					"url": src,
					"resourceEncodeName": enSrc,
					"resourceBlob": blob
				});

			request.onsuccess = function(event) {
				console.log('资源更新成功');
			};

			request.onerror = function(event) {
				console.log('资源更新失败');
			}
		};
		xhr.send();
	},

	/**
	 * 通过资源路径去查找缓存资源
	 * 无论是否找到，都会自动装配js
	 * @param {Object} src 资源路径
	 * @param {Object} callback 装配js后的回调函数
	 */
	queryCache: function(src, callback) {
		let me = this;
		let db = me.db;
		let dbName = me.dbName;
		var script = document.createElement("script");
		let enSrc = window.btoa(src);
		var transaction = db.transaction([dbName], 'readonly');
		var objectStore = transaction.objectStore(dbName);
		var request = objectStore.get(enSrc);
		request.onerror = function(event) {
			me.isCacheReady = false;
			script.src = src;
			document.getElementsByTagName("head").item(0).appendChild(script);
			script.onload = script.onreadystatechange = callback;
		};
		request.onsuccess = function(event) {
			if (request.result) {
				console.log("命中缓存："+src);
				script.src = window.URL.createObjectURL(request.result.resourceBlob);
			} else {
				let xhr = new XMLHttpRequest();
				xhr.open('GET', src, true);
				xhr.responseType = 'blob';
				xhr.onload = function() {
					let blob = new Blob([xhr.response]);
					me.addCache({
						"url": src,
						"resourceEncodeName": enSrc,
						"resourceBlob": blob
					});
					console.log("未命中缓存："+src);
					script.src = window.URL.createObjectURL(blob);
				};
				xhr.send();
			}
			document.getElementsByTagName("head").item(0).appendChild(script);
			script.onload = script.onreadystatechange = callback;
		};
	},

	/**
	 * 返回缓存所有的数据
	 */
	showAllCache: function() {
		let me = this;
		let db = me.db;
		let dbName = me.dbName;
		var objectStore = db.transaction(dbName).objectStore(dbName);
		objectStore.openCursor().onsuccess = function(event) {
			var cursor = event.target.result;
			if (cursor) {
				console.log(cursor);
				cursor.continue();
			} else {
				console.log('没有更多数据了！');
			}
		};
	},
	/**
	 * 获取zcacheSource的配置文件，以便更新资源
	 * @param {Object} url
	 * @param {Object} callback
	 */
	getResource: function(url, callback) {
		let time=1;
		let xhr = new XMLHttpRequest();
		xhr.open("GET", url);
		xhr.send();
		xhr.onload = xhr.onreadystatechange = function() {
			if (this.status == 200) {
				if(xhr.readyState==4&&time){
					time--;
					callback(eval("(" + this.response + ")"));
				}
			} else {
				throw new Error("加载失败");
			}
		}
	},
}
zcacheSource = new zcacheSource();
//zcacheSource.init();
if (typeof(zcache) != "undefined") {
	//将zcacheSource类合并到zcache类（如果存在）
	Object.assign(zcache, zcacheSource);
}
