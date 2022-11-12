
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.select.effects
    (:require [candy.api                      :refer [return]]
              [elements.input.events          :as input.events]
              [elements.input.subs            :as input.subs]
              [elements.select.config         :as select.config]
              [elements.select.events         :as select.events]
              [elements.select.prototypes     :as select.prototypes]
              [elements.select.views          :as select.views]
              [elements.text-field.helpers    :as text-field.helpers]
              [elements.text-field.prototypes :as text-field.prototypes]
              [re-frame.api                   :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/active-button-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id select-props]]
      {:db (r select.events/select-will-mount db select-id select-props)}))

(r/reg-event-fx :elements.select/active-button-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id _]]
      ; XXX#8706
      {:db (r input.events/unmark-as-visited! db select-id)}))

(r/reg-event-fx :elements.select/select-options-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      [:elements.select/reg-keypress-events! select-id select-props]))

(r/reg-event-fx :elements.select/select-options-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  (fn [{:keys [db]} [_ select-id {:keys [layout] :as select-props}]]
      ; XXX#8706
      {:db (case layout :select (r input.events/mark-as-visited! db select-id)
                                (return                          db))
       :dispatch [:elements.select/remove-keypress-events! select-id select-props]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/ESC-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [:ui/remove-popup! :elements.select/options])

(r/reg-event-fx :elements.select/ENTER-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id select-props]]
      (if-let [option-field-focused? (r input.subs/input-focused? db :elements.select/option-field)]
              [:elements.select/add-option! select-id select-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/reg-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id select-props]]
      (let [on-escape-props {:key-code 27 :required? true :on-keyup [:elements.select/ESC-pressed   select-id select-props]}
            on-enter-props  {:key-code 13 :required? true :on-keyup [:elements.select/ENTER-pressed select-id select-props]}]
           {:dispatch-n [[:environment/reg-keypress-event! ::on-ESC-pressed   on-escape-props]
                         [:environment/reg-keypress-event! ::on-ENTER-pressed on-enter-props]]})))

(r/reg-event-fx :elements.select/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id _]]
      {:dispatch-n [[:environment/remove-keypress-event! ::on-ESC-pressed]
                    [:environment/remove-keypress-event! ::on-ENTER-pressed]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/render-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      [:ui/render-popup! :elements.select/options
                         {:content [select.views/select-options select-id select-props]}]))

(r/reg-event-fx :elements.select/render-select!
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ;
  ; @usage
  ;  [:elements.select/render-select! {...}]
  ;
  ; @usage
  ;  [:elements.select/render-select! :my-select {...}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ select-id select-props]]
      ; Az [:elements.select/render-select! ...] eseményt önállóan is lehet használni
      ; a select komponens használata nélkül, ezért ...
      ; ... alkalmazza a select-props-prototype függvényt.
      ; ... alkalmazza az init-element! függvényt.
      (let [select-props (select.prototypes/select-props-prototype select-id select-props)]
           {:db       (r select.events/select-will-mount  db select-id select-props)
            :dispatch [:elements.select/render-options! select-id select-props]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ; @param (*) option
  (fn [{:keys [db]} [_ select-id {:keys [autoclear? option-value-f on-select] :as select-props} option]]
      (let [option-value (option-value-f option)]
           {:db             (r select.events/select-option! db select-id select-props option)
            :dispatch-later [               {:ms select.config/CLOSE-POPUP-DELAY     :dispatch [:ui/remove-popup! :elements.select/options]}
                             (if autoclear? {:ms select.config/AUTOCLEAR-VALUE-DELAY :dispatch [:elements.select/clear-value! select-id select-props]})
                             (if on-select  {:ms select.config/ON-SELECT-DELAY       :dispatch (r/metamorphic-event<-params on-select option-value)})]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.select/add-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id {:keys [add-option-f] :as select-props}]]
      (if-let [option-field-filled? (text-field.helpers/field-filled? :elements.select/option-field)]
              (let [field-id      :elements.select/option-field
                    field-props   (text-field.prototypes/field-props-prototype field-id {})
                    field-content (text-field.helpers/get-field-content        field-id)
                    option        (add-option-f field-content)]
                   {:db       (r select.events/add-option! db select-id select-props option)
                    :dispatch [:elements.text-field/empty-field! field-id field-props]}))))
