new Vue({
    el:'#app',
    data:{
        products:[
            {product_name:'xxx',prouct_num:6},
            {product_name:'aa',prouct_num:6},
            {product_name:'bb',prouct_num:6}
        ]
    },mounted(){
        this.$http
            .get('http://localhost:8381/productController/getAllProduct')
            .then(function(response){
                this.products=response.data;
                console.log(response.data)
            })
            .catch(function (error) { // 请求失败处理
                console.log(error);
            });
    }
})