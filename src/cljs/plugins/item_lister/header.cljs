(ns plugins.item-lister.header
  (:require [x.app-core.api :as a]
            [x.app-elements.api :as elements]
            [app-fruits.reagent :refer [ratom lifecycles]]))



;; ----------------------------------------------------------------------------
;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sort-by-button [lister-id lister-props]
  [elements/button {:layout :icon-button :icon :sort  :variant :transparent :color :none}])

(defn add-client-button [lister-id lister-props]
  [elements/button {:layout :icon-button :icon :add :variant :transparent}])

(defn search-field [lister-id lister-props]
  [elements/search-field {:placeholder :search}])

(defn search-bar [lister-id lister-props]
  [elements/row
   {:content [:<> [:div]
                  [add-client-button lister-id lister-props]
                  [elements/separator {:orientation :vertical :size :l}]
                  [sort-by-button    lister-id lister-props]
                  [elements/separator {:orientation :vertical :size :l}]
                  [elements/separator {:orientation :vertical :size :xxl}]
                  [search-field      lister-id lister-props]
                  [elements/separator {:orientation :vertical :size :l}]]
    :horizontal-align :center}])

;; ----------------------------------------------------------------------------
;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------
