
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.button.effects
    (:require [elements.button.events :as button.events]
              [re-frame.api           :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.button/button-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)}
  (fn [{:keys [db]} [_ button-id {:keys [keypress] :as button-props}]]
      (if keypress {:db       (r button.events/button-did-mount  db button-id button-props)
                    :dispatch [:elements.button/reg-keypress-event! button-id button-props]})))

(r/reg-event-fx :elements.button/button-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)}
  (fn [{:keys [db]} [_ button-id {:keys [keypress] :as button-props}]]
      (if keypress {:db (r button.events/button-did-update db button-id button-props)})))

(r/reg-event-fx :elements.button/button-will-unmount
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

(r/reg-event-fx :elements.button/reg-keypress-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)
  ;    {:key-code (integer)
  ;     :required? (boolean)(opt)
  ;      Default: false}}
  (fn [_ [_ button-id {:keys [keypress]}]]
      [:x.environment/reg-keypress-event! button-id
                                          {:key-code   (:key-code keypress)
                                           :on-keydown [:elements.button/key-pressed  button-id]
                                           :on-keyup   [:elements.button/key-released button-id]
                                           :required?  (:required? keypress)}]))

(r/reg-event-fx :elements.button/remove-keypress-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  (fn [_ [_ button-id _]]
      [:x.environment/remove-keypress-event! button-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.button/key-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  (fn [{:keys [db]} [_ button-id]]
      {:fx [:elements.button/focus-button! button-id]}))

(r/reg-event-fx :elements.button/key-released
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  (fn [{:keys [db]} [_ button-id]]
      (let [on-click (get-in db [:elements :element-handler/meta-items button-id :on-click])]
           {:dispatch on-click :fx [:elements.button/blur-button! button-id]})))
