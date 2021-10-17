
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.25
; Description:
; Version: v0.1.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text
    (:require [mid-fruits.candy          :refer [param]]
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
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :horizontal-align (keyword)
  ;   :layout (keyword)}
  [text-props]
  (merge {:color            :default
          :font-size        :s
          :font-weight      :normal
          :horizontal-align :left
          :layout           :row}
         (param text-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ;
  ; @return (map)
  [db [_ text-id]]
  (r engine/get-element-view-props db text-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) view-props
  ;  {:content (metamorphic-content)}
  ;
  ; @return (hiccup)
  [text-id view-props]
  (let [content-props (components/extended-props->content-props view-props)]
       [:div.x-text (engine/element-attributes text-id view-props)
                    [:div.x-text--body [components/content text-id content-props]]]))

(defn view
  ; XXX#0439
  ; a text elemen megjelenített szöveg megtörik, ha nincs elegendő hely.
  ; A label elemen megjelenített szöveg nem törik meg akkor sem, ha nincs elegendő hely,
  ;
  ; @param (keyword)(opt) text-id
  ; @param (map) text-props
  ;  {:class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :default
  ;   :content (metamorphic-content)(opt)
  ;    XXX#8711
  ;   :content-props (map)(opt)
  ;    XXX#8711
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :font-weight (keyword)(opt)
  ;    :bold, :extra-bold, :normal
  ;    Default: :normal
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :left
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/text {...}]
  ;
  ; @usage
  ;  [elements/text :my-text {...}]
  ;
  ; @return (component)
  ([text-props]
   [view nil text-props])

  ([text-id text-props]
   (let [text-id    (a/id   text-id)
         text-props (a/prot text-props text-props-prototype)]
        [engine/container text-id
          {:base-props text-props
           :component  text
           :subscriber [::get-view-props text-id]}])))
