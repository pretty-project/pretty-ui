
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.popups.effects
    (:require [re-frame.api           :as r]
              [x.ui.popups.prototypes :as popups.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.ui/render-popup-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  (fn [_ [_ popup-id popup-props]]
      {:dispatch-n [[:x.ui/request-rendering-element! :popups popup-id popup-props]
                    ; A popup-id azonosítójú popup felület által elhelyez egy scroll-tiltást
                    [:x.environment/add-scroll-prohibition! popup-id]]}))

(r/reg-event-fx :x.ui/render-popup!
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ;  {:content (metamorphic-content)
  ;   :on-mount (metamorphic-event)(opt)
  ;   :on-unmount (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [:x.ui/render-popup! {...}]
  ;
  ; @usage
  ;  [:x.ui/render-popup! :my-popup {...}]
  ;
  ; @usage
  ;  (defn my-content [popup-id] [:div "My content"])
  ;  [:x.ui/render-popup! {:content #'my-content}]
  [r/event-vector<-id]
  (fn [_ [_ popup-id popup-props]]
      (let [popup-props (popups.prototypes/popup-props-prototype popup-props)]
           [:x.ui/render-popup-element! popup-id popup-props])))

(r/reg-event-fx :x.ui/remove-popup!
  ; @param (keyword) popup-id
  ;
  ; @usage
  ;  [:x.ui/remove-popup! :my-popup]
  (fn [{:keys [db]} [_ popup-id]]
      {:dispatch-n [[:x.ui/destroy-element! :popups popup-id]
                    ; Eltávolítja a popup-id azonosítójú popup felület által elhelyezett scroll-tiltást
                    [:x.environment/remove-scroll-prohibition! popup-id]]}))
