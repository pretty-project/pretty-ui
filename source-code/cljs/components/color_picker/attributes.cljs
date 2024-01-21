
(ns components.color-picker.attributes
    (:require [dom.api              :as dom]
              [pretty-build-kit.api :as pretty-build-kit]
              [re-frame.api         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn placeholder-attributes
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [picker-id {:keys [disabled? on-select value-path]}]
  {:class                :c-color-picker--placeholder
   :data-color           :default
   :data-font-size       :xs
   :data-font-weight     :medium
   :data-letter-spacing  :auto
   :data-line-height     :text-block
   :data-text-selectable false})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn picked-color-attributes
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; {}
  ; @param (string) color
  ;
  ; @return (map)
  ; {}
  [_ {{:keys [border-radius height width]} :color-stamp} color]
  {:class :c-color-picker--color-stamp
   :data-border-radius border-radius
   :data-block-height  height
   :data-block-width   width
   :style {:background-color color}})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn picker-body-attributes
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; {:color-stamp (map)
  ;   {:gap (keyword, px or string)(opt)}
  ;  :on-select (Re-Frame metamorphic-event)(opt)
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [picker-id {{:keys [gap]} :color-stamp :keys [on-select value-path] :as picker-props}]
  (let [selector-props {:on-select on-select :value-path value-path}
        on-click       [:pretty-elements.color-selector/render-selector! picker-id selector-props]]
       (-> {:class           :c-color-picker--body
            :data-column-gap gap
            :on-click        #(r/dispatch on-click)
            :on-mouse-up     #(dom/blur-active-element!)}
           (pretty-build-kit/effect-attributes picker-props)
           (pretty-build-kit/indent-attributes picker-props)
           (pretty-build-kit/style-attributes  picker-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn picker-attributes
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (map)
  ; {}
  [_ picker-props]
  (-> {:class :c-color-picker}
      (pretty-build-kit/class-attributes   picker-props)
      (pretty-build-kit/outdent-attributes picker-props)
      (pretty-build-kit/state-attributes   picker-props)))
