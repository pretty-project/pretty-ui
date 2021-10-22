
(ns plugins.item-lister.header
    (:require [app-fruits.reagent         :refer [ratom lifecycles]]
              [mid-fruits.css             :as css]
              [x.app-core.api             :as a :refer [r]]
              [x.app-elements.api         :as elements]
              [plugins.item-lister.engine :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-SORT-BY :by-name)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sort-by-button
  [lister-id lister-props]
  [elements/button {:layout :icon-button :icon :sort :variant :transparent :color :none
                    :label :sort-by :on-click [:item-lister/render-sort-by-options! lister-id]}])

(defn add-new-button
  [lister-id lister-props]
  [elements/button {:layout :icon-button :icon :add :variant :transparent
                    :label :add-new! :on-click [:x.app-router/go-to! "új elem és új kategória"]}])

(defn select-more-button
  [lister-id lister-props]
  [elements/button {:layout :icon-button :variant :transparent :icon :radio_button_unchecked
                    :color :none :disabled? false :label :check}])

(defn delete-selected-button
  [lister-id lister-props]
  [elements/button {:layout :icon-button :variant :transparent :icon :delete_outline
                    :color :warning :disabled? true :label :delete!}])

(defn search-field
  [lister-id {:keys [on-search] :as lister-props}]
  [:div.item-lister--search-bar--search-field
    [elements/search-field {:placeholder :search :layout :fit :stretch-orientation :horizontal
                            :on-type-ended on-search}]])

(defn search-bar
  [lister-id {:keys [on-search] :as lister-props}]
  [:div.item-lister--search-bar
    [:div.item-lister--search-bar--buttons
      [add-new-button         lister-id lister-props]
      [sort-by-button         lister-id lister-props]
      [select-more-button     lister-id lister-props]
      [delete-selected-button lister-id lister-props]]
    (if (some? on-search)
        [search-field lister-id lister-props])])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/render-sort-by-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ lister-id]]
      (let [sortable? (r engine/get-lister-prop db lister-id :sortable?)]
           [:x.app-elements/render-select-options!
             ::order-by-select-options
             {:default-value DEFAULT-SORT-BY
              :options-label :order-by
              :options [{:label :by-name  :value :by-name}
                        {:label :by-date  :value :by-date}
                        {:label :by-size  :value :by-size}
                        (if sortable? {:label :by-order :value :by-order})]
              :value-path (engine/lister-prop-path lister-id :sort-by)}])))
