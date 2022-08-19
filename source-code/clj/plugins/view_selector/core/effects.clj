
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.effects
    (:require [plugins.view-selector.core.prototypes :as core.prototypes]
              [x.server-core.api                     :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/init-selector!
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:base-route (string)(opt)
  ;   :on-route (metamorphic-event)(opt)
  ;   :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:view-selector/init-selector! :my-selector {...}]
  (fn [_ [_ selector-id {:keys [base-route] :as selector-props}]]
      (let [selector-props (core.prototypes/selector-props-prototype selector-id selector-props)]
           {:dispatch-n [[:view-selector/reg-transfer-selector-props! selector-id selector-props]
                         (if base-route [:view-selector/add-base-route!     selector-id selector-props])
                         (if base-route [:view-selector/add-extended-route! selector-id selector-props])]})))
