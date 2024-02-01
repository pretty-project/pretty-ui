
(ns pretty-elements.expandable.views
    (:require [fruits.random.api                     :as random]
              [metamorphic-content.api               :as metamorphic-content]
              [pretty-elements.expandable.attributes :as expandable.attributes]
              [pretty-elements.expandable.env        :as expandable.env]
              [pretty-elements.expandable.prototypes :as expandable.prototypes]
              [pretty-elements.engine.api                     :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                           :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- expandable-header
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {}
  [expandable-id {:keys [icon label] :as expandable-props}]
  [:button (expandable.attributes/expandable-header-attributes expandable-id expandable-props)
           (if icon  [:i (expandable.attributes/expandable-icon-attributes expandable-id expandable-props) icon])
           (if label [:div {:class               :pe-expandable--label
                            :data-font-size      :s
                            :data-font-weight    :medium
                            :data-letter-spacing :auto
                            :data-line-height    :text-block}
                           (metamorphic-content/compose label)])
           (if (expandable.env/expanded? expandable-id)
               [:i {:class :pe-expandable--expand-icon :data-icon-family :material-symbols-outlined :data-icon-size :m} :expand_less]
               [:i {:class :pe-expandable--expand-icon :data-icon-family :material-symbols-outlined :data-icon-size :m} :expand_more])])

(defn expandable
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:content (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [expandable-id {:keys [content placeholder] :as expandable-props}]
  [:div (expandable.attributes/expandable-attributes expandable-id expandable-props)
        [expandable-header                           expandable-id expandable-props]
        (if (expandable.env/expanded? expandable-id)
            [:div (expandable.attributes/expandable-body-attributes expandable-id expandable-props)
                  [metamorphic-content/compose content placeholder]])])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    expandable-id expandable-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount expandable-id expandable-props))
                       :reagent-render         (fn [_ expandable-props] [expandable expandable-id expandable-props])}))

(defn view
  ; @param (keyword)(opt) expandable-id
  ; @param (map) expandable-props
  ; {:class (keywords or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :expanded? (boolean)(opt)
  ;   Default: true
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;   Default: :inherit
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-symbols-outlined
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :label (metamorphic-content)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [expandable {...}]
  ;
  ; @usage
  ; [expandable :my-expandable {...}]
  ([expandable-props]
   [view (random/generate-keyword) expandable-props])

  ([expandable-id expandable-props]
   ; @note (tutorials#parameterizing)
   (fn [_ expandable-props]
       (let [expandable-props (pretty-presets.engine/apply-preset               expandable-id expandable-props)
             expandable-props (expandable.prototypes/expandable-props-prototype expandable-id expandable-props)]
            [view-lifecycles expandable-id expandable-props]))))
