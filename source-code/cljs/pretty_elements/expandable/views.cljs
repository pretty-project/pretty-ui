
(ns pretty-elements.expandable.views
    (:require [fruits.random.api                     :as random]
              [metamorphic-content.api               :as metamorphic-content]
              [pretty-elements.expandable.attributes :as expandable.attributes]
              [pretty-elements.expandable.env        :as expandable.env]
              [pretty-elements.expandable.prototypes :as expandable.prototypes]
              [pretty-presets.api                    :as pretty-presets]))

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
  ; {:content (metamorphic-content)(opt)}
  [expandable-id {:keys [content] :as expandable-props}]
  [:div (expandable.attributes/expandable-attributes expandable-id expandable-props)
        [expandable-header                           expandable-id expandable-props]
        (if (expandable.env/expanded? expandable-id)
            [:div (expandable.attributes/expandable-body-attributes expandable-id expandable-props)
                  [metamorphic-content/compose content]])])

(defn element
  ; @param (keyword)(opt) expandable-id
  ; @param (map) expandable-props
  ; {:class (keywords or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :content-value-f (function)(opt)
  ;   Default: return
  ;  :disabled? (boolean)(opt)
  ;  :expanded? (boolean)(opt)
  ;   Default: true
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :icon-family (keyword)(opt)
  ;   :material-symbols-filled, :material-symbols-outlined
  ;   Default: :material-symbols-outlined
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :placeholder (metamorphic-content)(opt)
  ;  :placeholder-value-f (function)(opt)
  ;   Default: return
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [expandable {...}]
  ;
  ; @usage
  ; [expandable :my-expandable {...}]
  ([expandable-props]
   [element (random/generate-keyword) expandable-props])

  ([expandable-id expandable-props]
   ; @note (tutorials#parametering)
   (fn [_ expandable-props]
       (let [expandable-props (pretty-presets/apply-preset                      expandable-props)
             expandable-props (expandable.prototypes/expandable-props-prototype expandable-props)]
            [expandable expandable-id expandable-props]))))
