Vue.component('list',{
  template: '<div>A custom component!</div>'
})
var app2 = new Vue({
  el: '#list',
  data: {
    message: '页面加载于 ' + new Date()
  }
})