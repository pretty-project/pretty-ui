
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.effects
    (:require [x.app-core.api               :as a :refer [r]]
              [x.app-elements.select.config :as select.config]
              [x.app-elements.select.events :as select.events]
              [x.app-elements.select.views  :as select.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.select/reg-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ _]]
      (let [on-escape-props {:key-code 27 :required? true :on-keyup [:ui/close-popup! :elements.select/options]}]
           {:dispatch-n [[:environment/reg-keypress-event! ::on-escape-pressed on-escape-props]]})))

(a/reg-event-fx
  :elements.select/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  (fn [{:keys [db]} [_ select-id _]]
      {:dispatch-n [[:environment/remove-keypress-event! ::on-escape-pressed]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.select/render-options!
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ;
  ; @usage
  ;  [:elements.select/render-options! {...}]
  ;
  ; @usage
  ;  [:elements.select/render-options! :my-select {...}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ select-id select-props]]
      ; Az [:elements.select/render-options! ...] eseményt önállóan is lehet használni
      ; a select komponens használata nélkül, ezért ez az esemény is alkalmazza
      ; az init-element! függvényt, hogy beállítsa a kezdeti értékeket.
      {:db       (r select.events/init-element! db select-id select-props)
       :dispatch [:ui/render-popup! :elements.select/options
                                    {:content           [select.views/select-options              select-id select-props]
                                     :on-popup-closed   [:elements.select/remove-keypress-events! select-id select-props]
                                     :on-popup-rendered [:elements.select/reg-keypress-events!    select-id select-props]}]}))



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
  (fn [{:keys [db]} [_ select-id {:keys [autoclear? get-value-f on-popup-closed on-select] :as select-props} option]]
      (let [option-value (get-value-f option)]
           {:db             (r select.events/select-option! db select-id select-props option)
            :dispatch       (a/metamorphic-event<-params on-select option-value)
            :dispatch-later [                    {:ms select.config/CLOSE-POPUP-DELAY     :dispatch [:ui/close-popup! :elements.select/options]}
                             (if autoclear?      {:ms select.config/AUTOCLEAR-VALUE-DELAY :dispatch [:elements.select/clear-value! select-id select-props]})
                             (if on-popup-closed {:ms select.config/ON-POPUP-CLOSED-DELAY :dispatch on-popup-closed})]})))
