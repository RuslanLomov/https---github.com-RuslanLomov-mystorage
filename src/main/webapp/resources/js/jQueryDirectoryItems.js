/*
 * jQueryFileTree Plugin
 *
 * @author - Cory S.N. LaViska - A Beautiful Site (http://abeautifulsite.net/) - 24 March 2008
 * @author - Dave Rogers - https://github.com/daverogers/jQueryFileTree
 *
 * Usage: $('.fileTreeDemo').fileTree({ options }, callback )
 *
 * TERMS OF USE
 *
 * This plugin is dual-licensed under the GNU General Public License and the MIT License and
 * is copyright 2008 A Beautiful Site, LLC.
 */

(function($, window) {
	var DirectoryItems;
	DirectoryItems = (function() {

		function DirectoryItems(el, args, callback) {
			// console.log("start FileTree func");
			// console.log(el);
			var $el, defaults;
			$el = $(el);
			// console.log($el);
			defaults = {
				root : '/',
				script : '/files/filetree',
				folderEvent : 'click',
				loadMessage : 'Loading...',
				errorMessage : 'Unable to get file tree information',
			};
			this.jqft = {
				container : $el
			};
			this.options = $.extend(defaults, args);
			this.callback = callback;
			// console.log("this.args.root: "+ args.root);
			// console.log("main FileTree func before showTree call func");
			this.showDirectoryItems($el, this.options.root);
			// console.log("main FileTree end func");
		}

		DirectoryItems.prototype.showDirectoryItems = function(el, dir) {
			// console.log("showTree start func");
			// console.log("dir: " + dir);
			var $el, _this, data, handleFail, handleResult, options, result;
			$el = $(el);
			// console.log($el)
			options = this.options;
			_this = this;
			data = {
				fakePath : dir,
				action : "directoryTree",
			};
			// console.log( data)
			// $.ajax({
			// url : 'ViewController',
			// type : 'Get',
			// // dataType : 'image/jpg',
			// data : data
			// }).done(
			// function(result) {
			// // $el.append(result);
			// //
			// var $div = $('div#file_img')
			//
			// var $img = $(
			// '<img src="data:image/png;base64,' + result
			// + '" />').appendTo($div);
			// // console.log("result:" + result);
			// // return handleResult(result);
			// }).fail(function() {
			// console.log("result: fail");
			// });
			handleResult = function(result) {
				// console.log("handleResult start func");

			};
			handleFail = function() {
				$el.append("<p>" + options.errorMessage + "</p>");
				return false;
			};
			// console.log("before ajax");
			return $.ajax({
				url : options.script,
				type : 'GET',
				dataType : 'json',
				data : data
			}).done(function(result) {
				// jQuery.each(result, function(i, val) {
				// console.log("inside ajax done");
				// });
				// console.log(result)
				// console.log("result.image: " + result.image);
				// console.log("result.html: " + result.html);
				// console.log("result: " + result);
				$el.append(result.html);
				// $el.find('object').height(150).width(150);

				$('.file_name_class').each(function() {
					$(this).mCustomScrollbar({
						axis : "x",
						theme : "dark-thin",
						autoHideScrollbar : true,
						autoExpandScrollbar : true,
						advanced : {
							autoExpandHorizontalScroll : true
						}
					});
				});

				// var $div = $('div#dir-image');
				// var $img = $(
				// '<img src="data:image/png;base64,'
				// + result.image + '" />')
				// .appendTo($div);
				// console.log(result.file);
				// return handleResult(result);
			}).fail(function() {
				// console.log("inside ajax fail");
				return handleFail();
			});
		};

		return DirectoryItems;
	})();
	return $.fn.extend({
		directoryItems : function(args, callback) {
			// console.log(args + " callback: " + callback);
			return this.each(function() {
				// console.log("inside fileTree main");
				var $this, data;
				$this = $(this);
				$this.data('directoryItems', (data = new DirectoryItems(this,
						args, callback)));
				// console.log(data);
			});
		}
	});
})(window.jQuery, window);
