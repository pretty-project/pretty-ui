
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.blank.views
    (:require [mid-fruits.random               :as random]
              [x.app-components.api            :as components]
              [x.app-elements.blank.helpers    :as blank.helpers]
              [x.app-elements.blank.prototypes :as blank.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- blank-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;  {}
  [blank-id {:keys [content] :as blank-props}]
  [:div.x-blank--body (blank.helpers/blank-body-attributes blank-id blank-props)
                      [components/content content]])

(defn- blank
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  [blank-id blank-props]
  [:div.x-blank (blank.helpers/blank-attributes blank-id blank-props)
                [blank-body                     blank-id blank-props]])

(defn element
  ; @param (keyword)(opt) blank-id
  ; @param (map) blank-props
  ;  {:content (metamorphic-content)(opt)
  ;   :class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}:style (map)(opt)
  ;     :style (map)(opt)}
  ;
  ; @usage
  ;  [blank {...}]
  ;
  ; @usage
  ;  [blank :my-blank {...}]
  ([blank-props]
   [element (random/generate-keyword) blank-props])

  ([blank-id blank-props]
   (let [];blank-props (blank.prototypes/blank-props-prototype blank-props)
        [blank blank-id blank-props])))
