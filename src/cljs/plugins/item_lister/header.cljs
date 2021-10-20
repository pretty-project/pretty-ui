(ns plugins.item-lister.header
  (:require [x.app-core.api :as a]
            [app-fruits.reagent :refer [ratom lifecycles]]))

(defn view []
  [:div "header"])