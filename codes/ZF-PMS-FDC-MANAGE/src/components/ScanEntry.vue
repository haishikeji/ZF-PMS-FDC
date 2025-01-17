<template>
    <div class="BarCodeScannerDiv">
        <input required="" type="text" class="input" v-model.trim="scanEntryData" ref="scanInput" @keydown="handleScanInput">
        <i v-show="scanEntryData" class="el-icon-circle-close cleanInputIcon"  @click="clearData()"></i>
        <span class="highlight"></span>
        <span class="bar"></span>
        <label>{{label}}</label>
    </div>
</template>
  
  <script>

  export default {
        name: 'scanEntry',
        data() {
            return {
                scanEntryData: null,
            };
        },
        props: {
            label:{
                type: String,
                default: '',
            },
            onFocus:{
                type: Boolean,
                default: false
            }

        },

        watch: {
            onFocus:{
                handler(newValue){
                    if(newValue){                    
                        this.setFocus();                    
                    }
                },
                immediate: true
            }
              
        },
      
        beforeDestroy() {
            this.$refs.scanInput.removeEventListener('keydown', this.handleScanInput);
            console.log('scanEntry beforeDestroy');
        },
        methods: {
            setFocus() {
                this.$nextTick(() => {
                    this.$refs.scanInput.focus();
                },1000);
            },

            handleScanInput(event) {

                const input = event.target;

                const inputValue = input.value;

                this.scanEntryData = inputValue;
           
                if (event.key === 'Enter') {
                    this.$emit('scanEntryFun', inputValue);
                    
                    setTimeout(() => {

                        input.value = '';

                        this.scanEntryData = '';

                    }, 1000);

                }

            },

            clearData() {
                this.scanEntryData = '';
                this.showData = '';
                this.$refs.scanInput.focus();
            }
    },
  
}
  
</script>

<style lang="css" scoped>
.BarCodeScannerDiv {
 position: relative;
}

.input {
 font-size: 20px;
 padding: 10px 10px 10px 5px;
 display: block;
 width: 100%;
 border: none;
 border-bottom: 1px solid #515151;
 background: transparent;
}

.input:focus {
 outline: none;
}

label {
 color: #999;
 font-size: 18px;
 font-weight: normal;
 position: absolute;
 pointer-events: none;
 left: 5px;
 top: 10px;
 transition: 0.2s ease all;
 -moz-transition: 0.2s ease all;
 -webkit-transition: 0.2s ease all;
}

.input:focus ~ label, .input:valid ~ label {
 top: -10px;
 font-size: 14px;
 color: #5264AE;
}

.bar {
 position: relative;
 display: block;
 width: 100%;
}

.bar:before, .bar:after {
 content: '';
 height: 2px;
 width: 0;
 bottom: 1px;
 position: absolute;
 background: #5264AE;
 transition: 0.2s ease all;
 -moz-transition: 0.2s ease all;
 -webkit-transition: 0.2s ease all;
}

.bar:before {
 left: 50%;
}

.bar:after {
 right: 50%;
}

.input:focus ~ .bar:before, .input:focus ~ .bar:after {
 width: 50%;
}

.highlight {
 position: absolute;
 height: 60%;
 width: 100px;
 top: 25%;
 left: 0;
 pointer-events: none;
 opacity: 0.5;
}

.input:focus ~ .highlight {
 animation: inputHighlighter 0.3s ease;
}

@keyframes inputHighlighter {
 from {
  background: #5264AE;
 }

 to {
  width: 0;
  background: transparent;
 }
}

.cleanInputIcon{
 font-size: 20px;
  color: gray;
  position: absolute;
  top: 15px;
  left: 95%;
  z-index: 990;
}
</style>
