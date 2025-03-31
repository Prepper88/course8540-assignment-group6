<template>
  <div class="container">
    <!-- search area -->
    <div class="search-area">
      <div class="search-bar">
        <select v-model="searchType">
          <option>Plan</option>
          <option>Text</option>
          <option>Email</option>
          <option>Phone</option>
        </select>
        <div class="search-input">
          <input v-model="keyword" @input="onInput" placeholder="Input key words to search" />

          <!-- search suggestion -->
          <ul v-if="suggestions.length" class="suggestion-box" ref="autocompleteRef">
            <li
              v-for="(suggestion, index) in suggestions"
              :key="index"
              @click="onChooseSuggestion(suggestion)"
              class="suggestion-item"
            >
              <div>
                <span>{{ suggestion }}</span>
              </div>
            </li>
          </ul>
        </div>
        <button @click="onSearch">Search</button>
      </div>

      <!-- search hints -->
      <div class="search-hints">
        <span v-if="spells.length > 0">Did you mean:</span>
        <button v-for="(spell, index) in spells" :key="index">{{ spell }}</button>
      </div>
    </div>

    <div class="re-container">
      <REItem
        v-for="(reItem, index) in reItems"
        :key="index"
        :name="reItem.name"
        :url="reItem.url"
        :results="reItem.results"
      ></REItem>
    </div>

    <div class="text-container">
      <TextItem :text-items="textItems"></TextItem>
    </div>

    <div class="plan-container">
      <PlanCard
        v-for="(plan, index) in plans"
        :key="index"
        :name="plan.name"
        :download="plan.download"
        :upload="plan.upload"
        :additionalFeatures="plan.additionalFeatures"
        :vendor="plan.vendor"
        :specialServices="plan.specialServices"
        :price="plan.price"
        :fullPrice="plan.fullPrice"
        :icons="plan.icons"
      />
    </div>
  </div>
</template>

<script>
import PlanCard from './PlanCard.vue'
import REItem from './REItem.vue'
import axios from 'axios'
import debounce from 'lodash/debounce'
import TextItem from './TextItem.vue'

const JAVA_END_URL_PREFIX = 'http://localhost:8080'

export default {
  name: 'MainVue',
  components: {
    PlanCard,
    REItem,
    TextItem,
  },
  data() {
    return {
      searchType: 'Plan',
      keyword: '',
      suggestions: [],
      spells: [],
      reItems: [],
      plans: [],
      textItems: [],
    }
  },
  methods: {
    onInput: debounce(async function () {
      if (!this.keyword.trim()) {
        this.suggestions = []
        return
      }
      this.suggestions = await this.fetchSuggestions(this.searchType, this.keyword)
    }, 500),
    onBlur() {
      this.suggestions = []
    },
    async fetchSuggestions() {
      const data = {
        searchType: this.searchType,
        keyword: this.keyword,
      }
      const url = JAVA_END_URL_PREFIX + '/api/autocomplete'

      try {
        const response = await axios.get(url, {
          params: data,
        })

        console.log('autocomplete: ', response.data)
        return response.data.slice(0,5)
      } catch (error) {
        console.log('autocomplete error: ' + error)
      }
    },
    onChooseSuggestion(suggestion) {
      this.keyword = suggestion
      this.suggestions = []
    },
    async onSearch() {
      const data = {
        searchType: this.searchType,
        keyword: this.keyword,
      }
      const url = JAVA_END_URL_PREFIX + '/api/search'

      try {
        const response = await axios.get(url, {
          params: data,
        })
        console.log('search result: ', response.data)
        this.spells = response.data.spellHints
        this.reItems = response.data.reItems
        this.textItems = response.data.textItems
        this.plans = response.data.plans
      } catch (error) {
        console.log('search error: ' + error)
      }
    },
    handleClickOutside(event) {
      const box = this.$refs.autocompleteRef
      if (box && !box.contains(event.target)) {
        this.suggestions = []
      }
    },
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside)
  },

  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside)
  },
}
</script>

<style scoped>
.container {
  max-width: 1500px;
  margin: auto;
  padding: 20px;
  font-family: Arial, sans-serif;
  background-color: #f8f9fa;
}

.search-area {
  width: 80%;
  margin: auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.search-bar {
  position: relative;
  display: flex;
  justify-content: center;
  gap: 20px;
  flex-wrap: nowrap;
  background: #fff;
  padding: 15px;
  max-height: 40px;
}

.search-bar input,
.search-bar button {
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 14px;
  box-sizing: border-box;
}

.search-bar select {
  padding: 10px 14px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-sizing: border-box;
}

.search-input {
  width: 80%;
  box-sizing: border-box;
}

.search-input input {
  width: 100%;
  height: 100%;
}

.search-bar button {
  background-color: #007bff;
  color: #fff;
  border: none;
  cursor: pointer;
}

.search-hints {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  padding: 15px;
}

.search-hints span {
  color: #ea4335;
  font-family: Arial, sans-serif;
  font-size: 18px;
  line-height: 24px;
}

.search-hints button {
  padding: 6px 14px;
  background-color: #e6f0ff;
  border: none;
  border-radius: 20px;
  color: #0056b3;
  font-size: 14px;
  cursor: pointer;
}

.suggestion-box {
  width: 100%;
  margin: 4px auto;
  box-sizing: border-box;
  top: 40px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  list-style: none;
  padding: 0;
  max-height: 300px;
  overflow-y: auto;
  z-index: 10;
}

.suggestion-item {
  display: flex;
  align-items: center;
  padding: 10px 16px;
  gap: 12px;
  cursor: pointer;
}

.suggestion-item:hover {
  background-color: #f2f2f2;
}

.plan-container {
  display: flex;
  width: 80%;
  margin: 30px auto;
  justify-content: flex-start;
  gap: 80px;
  flex-wrap: wrap;
  padding: 20px;
}

.re-container {
  width: 80%;
  margin: 30px auto;
}

.text-container {
  width: 80%;
  margin: 30px auto;
}

.text-header {
  display: flex;
  justify-content: space-between;
}

.text-header span {
  font-family: 'Segoe UI', Tahoma, sans-serif;

  padding: 20px;
}
</style>
