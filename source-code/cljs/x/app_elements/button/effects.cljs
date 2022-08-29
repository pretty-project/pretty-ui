
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.button.effects
    (:require [x.app-core.api               :as a :refer [r]]
              [x.app-elements.button.events :as button.events]
              [x.app-elements.engine.api    :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.button/button-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)}
  (fn [{:keys [db]} [_ button-id {:keys [keypress] :as button-props}]]
      (if keypress {:db       (r button.events/button-did-mount  db button-id button-props)
                    :dispatch [:elements.button/reg-keypress-event! button-id button-props]})))

(a/reg-event-fx
  :elements.button/button-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)}
  (fn [{:keys [db]} [_ button-id {:keys [keypress] :as button-props}]]
      (if keypress {:db (r button.events/button-did-update db button-id button-props)})))

(a/reg-event-fx
  :elements.button/button-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)}
  (fn [{:keys [db]} [_ button-id {:keys [keypress] :as button-props}]]
      (if keypress {:db       (r button.events/button-will-unmount  db button-id button-props)
                    :dispatch [:elements.button/remove-keypress-event! button-id button-props]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)
  ;    {:key-code (integer)
  ;     :required? (boolean)(opt)
  ;      Default: false}}
  :elements.button/reg-keypress-event!
  (fn [_ [_ button-id {:keys [keypress]}]]
      [:environment/reg-keypress-event! button-id
                                        {:key-code   (:key-code keypress)
                                         :on-keydown [:elements.button/key-pressed  button-id]
                                         :on-keyup   [:elements.button/key-released button-id]
                                         :required?  (:required? keypress)}]))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  :elements.button/remove-keypress-event!
  (fn [_ [_ button-id _]]
      [:environment/remove-keypress-event! button-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.button/key-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  (fn [{:keys [db]} [_ button-id]]
      {:fx [:elements.button/focus-button! button-id]}))

(a/reg-event-fx
  :elements.button/key-released
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  (fn [{:keys [db]} [_ button-id]]
      (let [on-click (get-in db [:elements :element-handler/meta-items button-id :on-click])]
           {:dispatch on-click :fx [:elements.button/blur-button! button-id]})))
