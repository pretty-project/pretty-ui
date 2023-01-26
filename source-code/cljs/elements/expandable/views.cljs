
(ns elements.expandable.views
    (:require [elements.expandable.attributes :as expandable.attributes]
              [elements.expandable.env        :as expandable.env]
              [elements.expandable.prototypes :as expandable.prototypes]
              [random.api                     :as random]
              [x.components.api               :as x.components]))

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
           (if label [:div {:class :e-expandable--label :data-font-size :s :data-font-weight :medium :data-line-height :text-block}
                           (x.components/content label)])
           (if (expandable.env/expanded? expandable-id)
               [:i {:class :e-expandable--expand-icon :data-icon-family :material-symbols-outlined :data-icon-size :m} :expand_less]
               [:i {:class :e-expandable--expand-icon :data-icon-family :material-symbols-outlined :data-icon-size :m} :expand_more])])

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
                  [x.components/content                             expandable-id content]])])

(defn element
  ; @param (keyword)(opt) expandable-id
  ; @param (map) expandable-props
  ; {:class (keywords or keywords in vector)(opt)
  ;  :content (metamorphic-content)
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
  ;   Same as the :indent property
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
   (let [expandable-props (expandable.prototypes/expandable-props-prototype expandable-props)]
        [expandable expandable-id expandable-props])))
