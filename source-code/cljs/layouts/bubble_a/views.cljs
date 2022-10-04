
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.bubble-a.views
    (:require [layouts.bubble-a.helpers    :as helpers]
              [layouts.bubble-a.prototypes :as prototypes]
              [reagent.api                 :as reagent]
              [re-frame.api                :as r]
              [x.app-components.api        :as components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bubble-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) layout-props
  [bubble-id {:keys [] :as layout-props}])

(defn layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) layout-props
  ;  {:body (metamorphic-content)
  ;    Default: :none
  ;   :on-mount (metamorphic-event)(opt)
  ;   :on-unmount (metamorphic-event)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [bubble-a/layout :my-bubble {...}]
  [bubble-id {:keys [on-mount on-unmount] :as layout-props}]
  (let [layout-props (prototypes/layout-props-prototype layout-props)]
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                            :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                            :reagent-render         (fn [_ _] [bubble-a bubble-id layout-props])})))
