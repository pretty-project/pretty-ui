
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.effects
    (:require [re-frame.api               :as r]
              [x.app-ui.popups.prototypes :as popups.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :ui/render-popup-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  (fn [_ [_ popup-id popup-props]]
      {:dispatch-n [[:ui/request-rendering-element! :popups popup-id popup-props]
                    ; A popup-id azonosítójú popup felület által elhelyez egy scroll-tiltást
                    [:environment/add-scroll-prohibition! popup-id]]}))

(r/reg-event-fx :ui/render-popup!
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ;  {:content (metamorphic-content)
  ;   :on-mount (metamorphic-event)(opt)
  ;   :on-unmount (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [:ui/render-popup! {...}]
  ;
  ; @usage
  ;  [:ui/render-popup! :my-popup {...}]
  ;
  ; @usage
  ;  (defn my-content [popup-id] [:div "My content"])
  ;  [:ui/render-popup! {:content #'my-content}]
  [r/event-vector<-id]
  (fn [_ [_ popup-id popup-props]]
      (let [popup-props (popups.prototypes/popup-props-prototype popup-props)]
           [:ui/render-popup-element! popup-id popup-props])))

(r/reg-event-fx :ui/remove-popup!
  ; @param (keyword) popup-id
  ;
  ; @usage
  ;  [:ui/remove-popup! :my-popup]
  (fn [{:keys [db]} [_ popup-id]]
      {:dispatch-n [[:ui/destroy-element! :popups popup-id]
                    ; Eltávolítja a popup-id azonosítójú popup felület által elhelyezett scroll-tiltást
                    [:environment/remove-scroll-prohibition! popup-id]]}))
