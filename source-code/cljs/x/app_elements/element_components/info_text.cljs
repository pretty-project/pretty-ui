

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.info-text
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) text-props
  ;
  ; @return (map)
  ;  {}
  [text-props]
  (merge {}
         (param text-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- info-text-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  [text-id {:keys [content]}]
  [:div.x-info-text--content (components/content content)])

(defn- info-text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:content (metamorphic-content)
  [text-id {:keys [content] :as text-props}]
  [:div.x-info-text (engine/element-attributes text-id text-props)
                    [engine/info-text-button  text-id {}]
                    [engine/info-text-content text-id {:info-text content}]])

(defn element
  ; @param (keyword)(opt) text-id
  ; @param (map) text-props
  ;  {:content (metamorphic-content)(opt)
  ;   :class (keyword or keywords in vector)(opt)
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
  ;  [elements/info-text {...}]
  ;
  ; @usage
  ;  [elements/info-text :my-info-text {...}]
  ([text-props]
   [element (a/id) text-props])

  ([text-id text-props]
   (let [text-props (text-props-prototype text-props)]
        [info-text text-id text-props])))
