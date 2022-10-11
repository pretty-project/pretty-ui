
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text.views
    (:require [mid-fruits.random              :as random]
              [mid-fruits.string              :as string]
              [x.app-components.api           :as components]
              [x.app-elements.label.views     :as label.views]
              [x.app-elements.text.helpers    :as text.helpers]
              [x.app-elements.text.prototypes :as text.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {:info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [info-text label]}]
  (if label [label.views/element {:info-text info-text
                                  :content   label}]))

(defn- text-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {:placeholder (metamorphic-content)(opt)}
  [_ {:keys [placeholder]}]
  [:div.x-text--placeholder {:data-selectable false}
                            (if placeholder (components/content placeholder)
                                            "\u00A0")])

(defn- text-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {:content (metamorphic-content)}
  [label-id {:keys [content]}]
  (let [content      (components/content content)
        content-rows (string/split       content "\n")]
       (letfn [(f [%1 %2 %3] (if (= 0 %2) (conj %1       %3)
                                          (conj %1 [:br] %3)))]
              (reduce-kv f [:div.x-text--content] content-rows))))

(defn- text-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {:content (metamorphic-content)
  ;   :placeholder (metamorphic-content)(opt)}
  [text-id {:keys [content] :as text-props}]
  ; XXX#9811
  [:div.x-text--body (text.helpers/text-body-attributes text-id text-props)
                     ; BUG#3400
                     ; Az empty? függvény alkalmazása előtt az str függvényt
                     ; szükséges használni, különben ha a components/content
                     ; függvény kimenete integer típusú, akkor az empty?
                     ; függvény hibát dob!
                     (if (-> content components/content str empty?)
                         [text-placeholder text-id text-props]
                         [text-content     text-id text-props])])

(defn- text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  [text-id text-props]
  [:div.x-text (text.helpers/text-attributes text-id text-props)
               [text-label                   text-id text-props]
               [text-body                    text-id text-props]])

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
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
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
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :max-lines (integer)(opt)
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
   (let [text-props (text.prototypes/text-props-prototype text-props)]
        [text text-id text-props])))
