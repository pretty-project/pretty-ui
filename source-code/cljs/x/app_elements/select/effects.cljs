
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.effects
    (:require [x.app-core.api                   :as a :refer [r]]
              [x.app-elements.engine.api        :as engine]
              [x.app-elements.select.config     :as select.config]
              [x.app-elements.select.events     :as select.events]
              [x.app-elements.select.prototypes :as select.prototypes]
              [x.app-elements.select.views      :as select.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.select/ESC-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [:ui/close-popup! :elements.select/options])

(a/reg-event-fx
  :elements.select/ENTER-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id select-props]]
      (if-let [new-option-field-focused? (r engine/field-focused? db :elements.select/new-option-field)]
              (if-let [new-option-field-filled? (r engine/field-filled? db :elements.select/new-option-field)]
                      [:elements.select/add-option! select-id select-props]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.select/reg-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id select-props]]
      (let [on-escape-props {:key-code 27 :required? true :on-keyup [:elements.select/ESC-pressed   select-id select-props]}
            on-enter-props  {:key-code 13 :required? true :on-keyup [:elements.select/ENTER-pressed select-id select-props]}]
           {:dispatch-n [[:environment/reg-keypress-event! ::on-ESC-pressed   on-escape-props]
                         [:environment/reg-keypress-event! ::on-ENTER-pressed on-enter-props]]})))

(a/reg-event-fx
  :elements.select/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id _]]
      {:dispatch-n [[:environment/remove-keypress-event! ::on-ESC-pressed]
                    [:environment/remove-keypress-event! ::on-ENTER-pressed]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.select/render-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [_ [_ select-id select-props]]
      [:ui/render-popup! :elements.select/options
                         {:content           [select.views/select-options              select-id select-props]
                          :on-popup-closed   [:elements.select/remove-keypress-events! select-id select-props]
                          :on-popup-rendered [:elements.select/reg-keypress-events!    select-id select-props]}]))

(a/reg-event-fx
  :elements.select/render-select!
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ;
  ; @usage
  ;  [:elements.select/render-select! {...}]
  ;
  ; @usage
  ;  [:elements.select/render-select! :my-select {...}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ select-id select-props]]
      ; Az [:elements.select/render-select! ...] eseményt önállóan is lehet használni
      ; a select komponens használata nélkül, ezért ...
      ; ... alkalmazza a select-props-prototype függvényt.
      ; ... alkalmazza az init-element! függvényt.
      (let [select-props (select.prototypes/select-props-prototype select-id select-props)]
           {:db       (r select.events/init-select!  db select-id select-props)
            :dispatch [:elements.select/render-options! select-id select-props]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.select/select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ; @param (*) option
  (fn [{:keys [db]} [_ select-id {:keys [autoclear? option-value-f on-popup-closed on-select] :as select-props} option]]
      (let [option-value (option-value-f option)]
           {:db             (r select.events/select-option! db select-id select-props option)
            :dispatch       (a/metamorphic-event<-params on-select option-value)
            :dispatch-later [                    {:ms select.config/CLOSE-POPUP-DELAY     :dispatch [:ui/close-popup! :elements.select/options]}
                             (if autoclear?      {:ms select.config/AUTOCLEAR-VALUE-DELAY :dispatch [:elements.select/clear-value! select-id select-props]})
                             (if on-popup-closed {:ms select.config/ON-POPUP-CLOSED-DELAY :dispatch (a/metamorphic-event<-params on-popup-closed option-value)})]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.select/add-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id {:keys [add-option-f] :as select-props}]]
      (let [option-label (r engine/get-field-value db :elements.select/new-option-field)
            option       (add-option-f option-label)]
           {:db (as-> db % (r engine/empty-field-value! % :elements.select/new-option-field)
                           (r select.events/add-option! % select-id select-props option))})))
