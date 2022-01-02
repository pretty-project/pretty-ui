
(ns app-extensions.trader.listener
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.format    :as format]
              [mid-fruits.map       :refer [dissoc-in]]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.trader.account  :as account]
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
         (assoc-in [:trader :listener :new-listener :conditions] [{:closed? {:label "then" :value true}}])))

(a/reg-event-db :trader/add-new-listener! add-new-listener!)

(defn- discard-new-listener!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (-> db (assoc-in  [:trader :listener :add-mode?] false)
         (dissoc-in [:trader :listener :new-listener])))

(a/reg-event-db :trader/discard-new-listener! discard-new-listener!)

(defn- resolve-new-listener-condition!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ condition-dex {:keys [value] :as selected-option}]]
  (let [condition-count   (count (get-in db [:trader :listener :new-listener :conditions]))
        last-condition?   (= condition-count (inc condition-dex))
        condition-closed? (boolean value)]
       (cond ; If condition is closed ...
             (boolean condition-closed?)
             ; ... then,
             (update-in db [:trader :listener :new-listener :conditions] vector/remove-nth-item (inc condition-dex))
             ; If condition is NOT closed  and condition is the last ...
             (and last-condition? (not condition-closed?))
             ; ... then,
             (update-in db [:trader :listener :new-listener :conditions] vector/conj-item {:closed? {:label "then" :value true}})
             ; *
             :else (return db))))

(a/reg-event-db :trader/resolve-new-listener-condition! resolve-new-listener-condition!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- listener-condition-close-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [new-listener]} condition-dex]
  (let [condition-closed? (get-in new-listener [:conditions condition-dex :closed? :value])]
       [elements/select {:as-button? true :font-weight :extra-bold :preset :default-button :indent :right
                         :get-label-f :label :label (if condition-closed? "then" "and")
                         :initial-options [{:label "then" :value true} {:label "and" :value false}]
                         :on-select  [:trader/resolve-new-listener-condition! condition-dex]
                         :value-path [:trader :listener :new-listener :conditions condition-dex :closed?]}]))

(defn- listener-condition-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [new-listener]} condition-dex {:keys [options value value-key]}]
  [elements/select {:as-button? true :font-weight :extra-bold :preset :default-button :indent :right
                    :get-label-f :label :initial-options options :initial-value value
                    :label      (get-in new-listener [:conditions condition-dex value-key :label])
                    :value-path [:trader :listener :new-listener :conditions condition-dex value-key]}])

(defn- listener-attributes-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id list-props]
  [:div {:style (styles/row {:justify-content "space-between"})}
        [elements/button {:label "Cancel" :indent :right :preset :default-button :on-click [:trader/discard-new-listener!]}]
        [elements/button {:label "Save"   :indent :right :preset :primary-button}]])

(defn- listener-action
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id {:keys [new-listener] :as list-props}]
  (let [conditions-count   (count (get new-listener :conditions))
        last-condition-dex (dec conditions-count)]
       [:<>
;             (if (= conditions-count 3))
;                 [elements/label {:content "then" :color :muted}])
;                 [listener-condition-close-select list-id list-props last-condition-dex])
             [elements/label {:content " ..." :color :muted :indent :right}]]))

(defn- listener-condition
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id {:keys [new-listener] :as list-props} dex]
  [:<> ; "If" label / "then or and" select
       (if (= dex 0)
           [elements/label {:content "If"  :color :muted :indent :right}])
       ; "symbol" select / "symbol" label
       (if (not= dex 0)
           [elements/label {:content (get-in new-listener [:conditions 0 :symbol :label]) :color :muted :indent :right}]
           [listener-condition-select list-id list-props dex
                                      {:options engine/SYMBOL-OPTIONS
                                       :value   engine/DEFAULT-SYMBOL
                                       :value-key :symbol}])
       ; "price is" label
       [elements/label {:content "price is" :color :muted :indent :right}]
       ; "movement" select
       [listener-condition-select list-id list-props dex
                                  {:options engine/MOVEMENT-OPTIONS
                                   :value   {:label "rising" :value :rising}
                                   :value-key :movement}]
       ; "after"
       [elements/label {:content "after" :color :muted :indent :right}]
       ; "direction" select
       [listener-condition-select list-id list-props dex
                                  {:options engine/DIRECTION-OPTIONS
                                   :value   {:label "at least" :value :at-least}
                                   :value-key :direction}]
       ; "of" label
       [elements/label {:content "of" :color :muted :indent :right}]
       ; "elapsed time" select
       [listener-condition-select list-id list-props dex
                                  {:options engine/ELAPSED-TIME-OPTIONS
                                   :value   {:label "420 minutes" :value 420}
                                   :value-key :elapsed-time}]
       ; "long and" label
       [elements/label {:content "long and" :color :muted :indent :right}]
       ; "pattern-volume" select
       [listener-condition-select list-id list-props dex
                                  {:options engine/PATTERN-VOLUME-OPTIONS
                                   :value   {:label "100 USD" :value 100}
                                   :value-key :pattern-volume}]
       ; "high" / "deep" label
       (let [selected-pattern (get-in new-listener [:conditions dex :pattern :value])]
            (case selected-pattern :rocky-mountains [elements/label {:content "high" :color :muted :indent :right}]
                                   :grand-canyon    [elements/label {:content "deep" :color :muted :indent :right}]
                                   nil))
       [listener-condition-select list-id list-props dex
                                  {:options engine/PATTERN-OPTIONS
                                   :value   {:label "Rocky Mountains" :value :rocky-mountains}
                                   :value-key :pattern}]
       ; "period" label
       [elements/label {:content "period," :color :muted :indent :right}]
       ;
       (if (= dex 2)
           [elements/label {:content "then," :color :muted :indent :right}]
           [listener-condition-close-select list-id list-props dex])])

(defn- listener-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id {:keys [new-listener] :as list-props}]
  [:div {:style {}}
        [:div {:style (styles/row)}
              (reduce-kv #(conj %1 ^{:key (str %3)} [listener-condition list-id list-props %2])
                          [:<>] (get new-listener :conditions))
              [listener-action list-id list-props]]
        [listener-attributes-buttons list-id list-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- new-listener-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [list-id list-props]
  [:div {:style (styles/overlay-center)}
        [listener-attributes list-id list-props]
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
  [:div {:style (styles/row)}
        [add-new-listener-button list-id list-props]
        [elements/button {:label "Send test order!" :preset :secondary-button
                          :on-click [:trader/send-test-order!]}]])

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

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/send-test-order!
  (fn [{:keys [db]} _]
      [:sync/send-query! :trader/synchronize!
                         {:query [`(:trader/create-order! ~(r account/get-api-details db))]}]))
