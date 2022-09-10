
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.breadcrumbs.views
    (:require [x.app-components.api                  :as components]
              [x.app-core.api                        :as a]
              [x.app-elements.breadcrumbs.helpers    :as breadcrumbs.helpers]
              [x.app-elements.breadcrumbs.prototypes :as breadcrumbs.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- breadcrumbs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  [breadcrumbs-id breadcrumbs-props]
  [:div.x-breadcrumbs (breadcrumbs.helpers/breadcrumbs-attributes breadcrumbs-id breadcrumbs-props)])

(defn element
  ; @param (keyword)(opt) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :breadcrumbs (maps in vector)
  ;    [{:href (string)(opt)
  ;      :label (metamorphic-content)
  ;      :on-click (metamorphic-event)(opt)}]
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/breadcrumbs {...}]
  ;
  ; @usage
  ;  [elements/breadcrumbs :my-breadcrumbs {...}]
  ([breadcrumbs-props]
   [element (a/id) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   (let [breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-props)]
        [breadcrumbs breadcrumbs-id breadcrumbs-props])))
