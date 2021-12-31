
(ns app-extensions.trader.listener
    (:require [mid-fruits.format    :as format]
              [mid-fruits.map       :refer [dissoc-in]]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]
              [app-extensions.trader.engine   :as engine]
              [app-extensions.trader.styles   :as styles]
              [mid-extensions.trader.listener :as listener]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def price-inc?              listener/price-inc?)
(def price-inc-from-minimum? listener/price-inc-from-minimum?)
(def drop-length             listener/drop-length)
(def price-drop              listener/price-drop)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def DEFAULT-SYMBOL {:label "ETH / USD" :value "ETHUSD"})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-listener-list-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :listener]))

(a/reg-sub :trader/get-listener-list-props get-listener-list-props)


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-new-listener!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (-> db (assoc-in [:trader :listener :add-mode?] true)
         (assoc-in [:trader :listener :new-listener :step] 1)
         (assoc-in [:trader :listener :conditons] [])))

(a/reg-event-db :trader/add-new-listener! add-new-listener!)

(defn- discard-new-listener!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (-> db (assoc-in  [:trader :listener :add-mode?] false)
         (dissoc-in [:trader :listener :new-listener])))

(a/reg-event-db :trader/discard-new-listener! discard-new-listener!)

(defn- confirm-new-listener-name!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (assoc-in db [:trader :listener :new-listener :step] 2))

(a/reg-event-db :trader/confirm-new-listener-name! confirm-new-listener-name!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- listener-attributes-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id list-props]
  [:div {:style (styles/row {:justify-content "space-between"})}
        [elements/button {:label "Cancel" :preset :default-button :on-click [:trader/discard-new-listener!]}]
        [elements/button {:label "Save"   :preset :primary-button}]])

(defn- listener-action
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id {:keys [new-listener] :as list-props}]
  [:div {:style (styles/row)}
        [elements/select {:as-button? true :font-weight :extra-bold
                          :label "then"
                          :preset :default-button :indent :right
                          :initial-options [{:label "then" :value true} {:label "and" :value false}]
                          :initial-value   {:label "then" :value true}
                          :get-label-f :label
                          :on-select [:trader/resolve-listener-condition!]}]
        [elements/label {:content "..." :color :muted}]])

(defn- listener-condition
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id {:keys [new-listener] :as list-props}]
  [:div {:style (styles/row)}
        [elements/label {:content "If" :color :muted}]
        [elements/select {:as-button? true :font-weight :extra-bold
                          :label (get-in new-listener [:symbol :label])
                          :preset :default-button :indent :both
                          :initial-options engine/SYMBOL-OPTIONS
                          :initial-value   DEFAULT-SYMBOL
                          :get-label-f :label
                          :value-path [:trader :listener :new-listener :symbol]}]
        [elements/label {:content "price" :color :muted}]
        [elements/select {:as-button? true :font-weight :extra-bold
                          :label (get new-listener :movement)
                          :preset :default-button :indent :both
                          :initial-value "rising"
                          :initial-options ["rising" "falling"]
                          :value-path [:trader :listener :new-listener :movement]}]
        [elements/label {:content "after" :color :muted}]
        [elements/select {:as-button? true :font-weight :extra-bold
                          :label (get new-listener :xxx)
                          :preset :default-button :indent :both
                          :initial-value "at least"
                          :initial-options ["at least" "at most"]
                          :value-path [:trader :listener :new-listener :xxx]}]
        [elements/label {:content "of" :color :muted}]
        [elements/select {:as-button? true :font-weight :extra-bold
                          :label (get new-listener :period-count)
                          :preset :default-button :indent :both
                          :initial-value "20"
                          :initial-options ["20" "40" "60" "80" "100" "120" "140" "160" "180" "200"]
                          :value-path [:trader :listener :new-listener :period-count]}]
        [elements/label {:content "x" :color :muted}]
        [elements/select {:as-button? true :font-weight :extra-bold
                          :label (get-in new-listener [:interval :label])
                          :preset :default-button :indent :both
                          :initial-value {:label "3 minutes" :value "3"}
                          :get-label-f :label
                          :initial-options engine/INTERVAL-OPTIONS
                          :value-path [:trader :listener :new-listener :interval]}]
        [elements/label {:content "long period," :color :muted}]])

(defn- listener-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id {:keys [new-listener] :as list-props}]
  [:div {:style {}}
        [listener-condition          list-id list-props]
        [listener-action             list-id list-props]
        [listener-attributes-buttons list-id list-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- new-listener-form-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id list-props]
  [:div {:style (styles/row {:justify-content "space-between"})}
        [elements/button ::submit-listener-name-button
                         {:label "Cancel" :preset :default-button :indent :both
                          :on-click [:trader/discard-new-listener!]}]
        [elements/button ::submit-listener-name-button
                         {:label "Next" :preset :primary-button :indent :both
                          :on-click [:trader/confirm-new-listener-name!]}]])

(defn- new-listener-form-step-1
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id {:keys [] :as list-props}]
  [:div [elements/text-field ::new-listener-name
                             {:label "Name" :initial-value "New listener" :min-width :xs
                              :value-path [:trader :listener :new-listener :name]}]
        [new-listener-form-buttons list-id list-props]])

(defn- new-listener-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id {:keys [new-listener] :as list-props}]
  [:div {:style (styles/overlay-center)}
        (if (= (:step new-listener) 1)
            [new-listener-form-step-1 list-id list-props]
            [listener-attributes      list-id list-props])
        [elements/horizontal-separator {:size :xxl}]
        [elements/horizontal-separator {:size :xxl}]])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-new-listener-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [add-mode?]}]
  [elements/button ::add-new-listener-button
                   (if add-mode? {:layout :icon-button :variant :placeholder}
                                 {:preset :add-icon-button
                                  :indent :left
                                  :on-click [:trader/add-new-listener!]})])

(defn- listener-list-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id list-props]
  [add-new-listener-button list-id list-props])

(defn- no-listeners-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/label {:content "No listeners to show"
                   :color :highlight}])

(defn- no-listeners-to-show
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id list-props]
  [:div {:style (styles/overlay-center)}
        [no-listeners-to-show-label list-id list-props]
        [elements/horizontal-separator {:size :xxl}]])

(defn- listener-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id {:keys [listener-ids] :as list-props}]
  (if (vector/nonempty? listener-ids)
      [listener-list        list-id list-props]
      [no-listeners-to-show list-id list-props]))

(defn- listener-list-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id {:keys [add-mode?] :as list-props}]
  [:div {:style (styles/listener-list-structure-style)}
        [:div {:style (styles/listener-list-body-style)}
              [listener-list-header list-id list-props]
              (cond add-mode? [new-listener-form list-id list-props]
                    :else     [listener-list     list-id list-props])]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber body-id
                         {:component  #'listener-list-structure
                          :subscriber [:trader/get-listener-list-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------
