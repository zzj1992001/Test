<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp"%>
<%@ include file="/WEB-INF/web/common/base_path.jsp"%>
<html>
<head>
  <meta charset="utf-8">
  <base href="<%=basePath %>">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Vue.js</title>
  <meta content="width=device-width, initial-scale=1" name="viewport">
  <link href="assets/css/vue.css" rel="stylesheet">
    <link href="assets/css/normalize.css" rel="stylesheet">
  <script src="https://unpkg.com/vue"></script>
</head>
<body>

	<div id="app">
		{{message}}
	</div>
	
	<div id="app2">
		<span v-bind:title="message">鼠标停留几秒钟，查看此处动态绑定的信息</span>
	</div>
	
	<div id="app-3">
		<p v-if="seen">现在你看到我了</p>
	</div>

	<div id="app-4">
		<ol>
			<li v-for="todo in todos">
				{{todo.text}}
			</li>
		</ol>
	</div>

	<div id="app-5">
		<p>{{message}}</p>
		<button v-on:click="reverseMessage">逆转消息</button>
	</div>

	<div id="app-6">
		<p>{{message}}</p>
		<input v-model="message">
	</div>
	
	<div id="app-7">
		<ol>
			<todo-item v-for="item in groceryList" v-bind:todo="item" v-bind:key="item.id">
			</todo-item>
		</ol>
	</div>

	<div id="app-8">
	{{number + 1 }}
	</div>

	<div id="example-1">
		<button v-on:click="counter += 1">增加1</button>
		<p>按钮被点击了{{counter}}次</p>
	</div>
	
	<!-- component template -->
	<script type="text/x-template" id="grid-template">
  <table>
    <thead>
      <tr>
        <th v-for="key in columns"
          @click="sortBy(key)"
          :class="{ active: sortKey == key }">
          {{ key | capitalize }}
          <span class="arrow" :class="sortOrders[key] > 0 ? 'asc' : 'dsc'">
          </span>
        </th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="entry in filteredData">
        <td v-for="key in columns">
          {{entry[key]}}
        </td>
      </tr>
    </tbody>
  </table>
	</script>
	
	<!-- demo root element -->
	<div id="demo">
	  <form id="search">
	    Search <input name="query" v-model="searchQuery">
	  </form>
	  <demo-grid
	    :data="gridData"
	    :columns="gridColumns"
	    :filter-key="searchQuery">
	  </demo-grid>
	</div>
	
	<div id="app-10"><p>
    Ask a yes/no question:
    <input v-model="question">
  </p>
  <p>{{ answer }}</p></div>
  
  <div id="app-11">
  <li v-for="n in evenNumbers">{{ n }}</li>
  </div>
  
  <script type="text/javascript">
  var app11 = new Vue({
	  el:'#app-11',
	  data: {
		  numbers: [ 1, 2, 3, 4, 5 ]
		},
		computed: {
		  evenNumbers: function () {
		    return this.numbers.filter(function (number) {
		      return number % 2 === 0
		    })
		  }
		}
  }) 
  </script>
  
	<script src="https://unpkg.com/axios@0.12.0/dist/axios.min.js"></script>
	<script src="https://unpkg.com/lodash@4.13.1/lodash.min.js"></script>
	<script type="text/javascript">
	var watchExampleVM = new Vue({
		el:'#app-10',
		data:{
			question:'',
			answer:'我不能给你答案 until 你提问'
		},
		watch: {
		    // 如果 question 发生改变，这个函数就会运行
		    question: function (newQuestion) {
		      this.answer = 'Waiting for you to stop typing...'
		      this.getAnswer()
		    }
	   }, 
	   methods:{
		   getAnswer: _.debounce(
		      function () {
		        if (this.question.indexOf('?') === -1) {
		          this.answer = 'Questions usually contain a question mark. ;-)'
		          return
		        }
		        this.answer = 'Thinking...'
		        var vm = this
		        axios.get('https://yesno.wtf/api')
		          .then(function (response) {
		            vm.answer = _.capitalize(response.data.answer)
		          })
		          .catch(function (error) {
		            vm.answer = 'Error! Could not reach the API. ' + error
		          })
		      },
		      // 这是我们为用户停止输入等待的毫秒数
		      50
		    )
	   }
	})
	</script>


	<script type="text/javascript">
		var app = new Vue({
			el: '#app',
			data: {
				message: 'Hell world'
			}
		});
	</script>
	<script type="text/javascript">
		var app2 = new Vue({
			el: '#app2',
			data: {
				message: 'Hell world'
			}
		});
	</script>
	<script type="text/javascript">
		var app3 = new Vue({
			el: '#app-3',
			data: {
				seen: true
			}
		});
	</script>
	<script type="text/javascript">
		var app4 = new Vue({
			el: '#app-4',
			data: {
				todos: [
					{text: '学习 Javascript'},
					{text: '学习 Vue'},
					{text: '整个项目'},
				] 
			}
		});		
	</script>
	<script type="text/javascript">
		var app5 = new Vue({
			el: '#app-5',
			data: {
				message: 'Hello Vue.js'
			},
			methods: {
				reverseMessage: function(){
					this.message = this.message.split('').reverse().join('');
				}
			}
		});
	</script>
	<script type="text/javascript">
		var app6 = new Vue({
			el: '#app-6',
			data: {
				message: 'Hello Vue.js'
			}
		});
	</script>
	<script type="text/javascript">
		Vue.component('todo-item', {
			props: ['todo'],
			template: '<li>{{todo.text}}</li>'
		});
		
		var app7 = new Vue({
			el: '#app-7',
			data: {
				groceryList: [
					{id: 0, text: '蔬菜'},
					{id: 1, text: '奶酪'},
					{id: 2, text: '随便其它什么人吃的东西'}
				]
			}
		});
	</script>
	<script type="text/javascript">
		var example1 = new Vue({
			el: '#example-1',
			data: {
				counter: 0
			}
		});
	</script>
	<script type="text/javascript">
		var app6 = new Vue({
			el: '#app-8',
			data: {
				number: 100
			}
		});
	</script>
	
	<script type="text/javascript">
	// register the grid component
	Vue.component('demo-grid', {
	  template: '#grid-template',
	  props: {
	    data: Array,
	    columns: Array,
	    filterKey: String
	  },
	  data: function () {
	    var sortOrders = {}
	    this.columns.forEach(function (key) {
	      sortOrders[key] = 1
	    })
	    return {
	      sortKey: '',
	      sortOrders: sortOrders
	    }
	  },
	  computed: {
	    filteredData: function () {
	      var sortKey = this.sortKey
	      var filterKey = this.filterKey && this.filterKey.toLowerCase()
	      var order = this.sortOrders[sortKey] || 1
	      var data = this.data
	      if (filterKey) {
	        data = data.filter(function (row) {
	          return Object.keys(row).some(function (key) {
	            return String(row[key]).toLowerCase().indexOf(filterKey) > -1
	          })
	        })
	      }
	      if (sortKey) {
	        data = data.slice().sort(function (a, b) {
	          a = a[sortKey]
	          b = b[sortKey]
	          return (a === b ? 0 : a > b ? 1 : -1) * order
	        })
	      }
	      return data
	    }
	  },
	  filters: {
	    capitalize: function (str) {
	      return str.charAt(0).toUpperCase() + str.slice(1)
	    }
	  },
	  methods: {
	    sortBy: function (key) {
	      this.sortKey = key
	      this.sortOrders[key] = this.sortOrders[key] * -1
	    }
	  }
	})

	// bootstrap the demo
	var demo = new Vue({
	  el: '#demo',
	  data: {
	    searchQuery: '',
	    gridColumns: ['name', 'power'],
	    gridData: [
	      { name: 'Chuck Norris', power: Infinity },
	      { name: 'Bruce Lee', power: 9000 },
	      { name: 'Jackie Chan', power: 7000 },
	      { name: 'Jet Li', power: 8000 }
	    ]
	  }
	})
	</script>
</body>
</html>