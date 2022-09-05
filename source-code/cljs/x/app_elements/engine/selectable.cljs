
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.selectable
    (:require [mid-fruits.candy              :refer [param return]]
              [mid-fruits.map                :as map]
              [mid-fruits.vector             :as vector]
              [x.app-components.api          :as components]
              [x.app-core.api                :as a :refer [r]]
              [x.app-db.api                  :as db]
              [x.app-elements.engine.element :as element]
              [x.app-elements.input.events   :as input.events]
              [x.app-elements.input.subs   :as input.subs]
              [x.app-environment.api         :as environment]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------


(defn on-select-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (function)
  [input-id option]
  #(a/dispatch [:elements/select-option! input-id option]))

(defn on-unselect-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (function)
  [input-id]
  #(a/dispatch [:elements/unselect-option! input-id]))

(defn selectable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:selected? (boolean)}
  ;
  ; @return (map)
  ;  {:data-selected (boolean)}
  [input-id {:keys [selected?] :as input-props}]
  (element/element-attributes input-id input-props {:data-selected (boolean selected?)}))

(defn selectable-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:disabled? (boolean)(opt)
  ;   :value (keyword)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ;  {:data-selected (boolean)
  ;   :disabled (boolean)
  ;   :on-click (function)}
  [input-id {:keys [disabled?] :as input-props} option]
  (let [selected-value (:value input-props)
        selected?      (= selected-value option)]
       (if disabled? {:data-selected (param selected?)
                      :disabled      (param true)}
                     {:data-selected (param selected?)
                      :on-click      (on-select-function input-id option)
                      :on-mouse-up  #(environment/blur-element!)})))

(defn selectable-unselect-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:selected? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-disabled (string)
  ;   :disabled (boolean)
  ;   :on-click (function)
  ;   :title (string)}
  [input-id {:keys [selected?]}]
  (if selected? {:on-click      (on-unselect-function input-id)
                 :on-mouse-up  #(environment/blur-element!)
                 :title         (components/content {:content :uncheck-selected!})}
                {:data-disabled (param true)
                 :disabled      (param true)}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selectable-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [db [_ input-id]]
  ; BUG#7633
  (if-let [options-path (r element/get-element-prop db input-id :options-path)]
          (get-in db options-path)))

(defn option-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (boolean)
  [db [_ input-id option]]
  (let [input-value (r input.subs/get-input-value db input-id)]
       (= input-value option)))

(a/reg-sub :elements/option-selected? option-selected?)

(defn selectable-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [value (r input.subs/get-input-value db input-id)]
       (not (nil? value))))

(defn selectable-nonselected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (not (r selectable-selected? db input-id)))

(defn get-selectable-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :helper (keyword)
  ;   :options (vector)
  ;   :selected? (boolean)
  ;   :value (keyword)}
  [db [_ input-id]]
  (merge {:options   (r get-selectable-options db input-id)
          :selected? (r selectable-selected?   db input-id)
          :value     (r input.subs/get-input-value  db input-id)}))
         ;(if (r input.subs/required-warning? db input-id)
          ;   {:border-color :warning
          ;    :helper       :please-select-an-option}))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- init-selectable!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (as-> db % (r input.events/init-input!               % input-id)))
             ; Már nem igy van használva: lsd. select, radio-button, ...
             ;(r use-selectable-initial-options! % input-id)))

(a/reg-event-db :elements/init-selectable! init-selectable!)

; XXX#NEW VERSION!
(defn add-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:new-option-f (function)
  ;   :options-path (vector)}
  ; @param (*) option-value
  ;
  ; @return (map)
  [db [_ _ {:keys [new-option-f options-path]} option-value]]
  (let [option (new-option-f option-value)]
       (update-in db options-path vector/cons-item-once option)))

; XXX#NEW VERSION!
(defn select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:get-value-f (function)
  ;   :value-path (vector)}
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ input-id {:keys [get-value-f value-path]} option]]
  (let [option-value (get-value-f option)]
       (assoc-in db value-path option-value)))

(defn unselect-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  [db [_ input-id]]
  (let [value-path (r element/get-element-prop db input-id :value-path)]
       (as-> db % (r db/remove-item!              % value-path)
                  (r input.events/mark-as-visited! % input-id))))

(a/reg-event-db :elements/unselect-option! unselect-option!)

(defn toggle-select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ input-id option]]
  (if (r option-selected? db input-id option)
      (r unselect-option! db input-id)
      (r select-option!   db input-id option)))

(a/reg-event-db :elements/toggle-select-option! toggle-select-option!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  (fn [{:keys [db]} [_ input-id option]]
      (let [on-select-event (r element/get-element-prop db input-id :on-select)
            get-value-f     (r element/get-element-prop db input-id :get-value-f)
            value           (get-value-f option)]
           {:db       (r select-option! db input-id option)
            :dispatch (a/metamorphic-event<-params on-select-event value)})))

(a/reg-event-fx
  :elements/reg-select-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ _]]
      (let [on-escape-props {:key-code 27 :required? true :on-keyup [:ui/close-popup! :elements/select-options]}]
           {:dispatch-n [[:environment/reg-keypress-event! ::on-ESC-pressed on-escape-props]]})))

(a/reg-event-fx
  :elements/remove-select-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      {:dispatch-n [[:environment/remove-keypress-event! ::on-ESC-pressed]]}))
