
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.text
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.random         :as random]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) text-props
  ;
  ; @return (map)
  ;  {:font-size (keyword)
  ;   :horizontal-align (keyword)
  ;   :selectable? (boolean)}
  [text-props]
  (merge {:font-size        :s
          :font-weight      :normal
          :horizontal-align :left
          :selectable?      true}
         (param text-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {:content (metamorphic-content)
  ;   :placeholder (metamorphic-content)(opt)}
  [text-id {:keys [content placeholder] :as text-props}]
  ; XXX#9811
  (let [content (components/content text-id content)]
       [:div.x-text (engine/element-attributes text-id text-props)
                    (if (empty? content)
                        (if placeholder [:div.x-text--placeholder (components/content text-id content)]
                                        [:div.x-text--placeholder "\u00A0"])
                        [:div.x-text--body content])]))

(defn element
  ; XXX#0439
  ; A text elemen megjelenített szöveg megtörik, ha nincs elegendő hely.
  ; A label elemen megjelenített szöveg nem törik meg akkor sem, ha nincs elegendő hely.
  ;
  ; @param (keyword)(opt) text-id
  ; @param (map) text-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :color (keyword or string)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :default
  ;   :content (metamorphic-content)(opt)
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :font-weight (keyword)(opt)
  ;    :bold, :extra-bold, :normal
  ;    Default: :normal
  ;   :horizontal-align (keyword)(opt)
  ;    :center, :left, :right
  ;    Default: :left
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :placeholder (metamorphic-content)(opt)
  ;   :selectable? (boolean)(opt)
  ;    Default: true
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/text {...}]
  ;
  ; @usage
  ;  [elements/text :my-text {...}]
  ([text-props]
   [element (random/generate-keyword) text-props])

  ([text-id text-props]
   (let [text-props (text-props-prototype text-props)]
        [text text-id text-props])))
