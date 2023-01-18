
(ns elements.expandable.views
    (:require [elements.expandable.helpers    :as expandable.helpers]
              [elements.expandable.prototypes :as expandable.prototypes]
              [pretty-css.api                 :as pretty-css]
              [random.api                     :as random]
              [x.components.api               :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- expandable-expand-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:expanded? (boolean)}
  [expandable-id {:keys [expanded?]}]
  (if (expandable.helpers/expanded? expandable-id)
      [:i.e-expandable--expand-icon {:data-icon-family :material-symbols-outlined :data-icon-size :m} :expand_less]
      [:i.e-expandable--expand-icon {:data-icon-family :material-symbols-outlined :data-icon-size :m} :expand_more]))

(defn- expandable-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:icon (keyword)(opt)}
  [expandable-id {:keys [icon] :as expandable-props}]
  (if icon [:i.e-expandable--icon (pretty-css/icon-attributes {} expandable-props)
                                  icon]))

(defn- expandable-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [:div.e-expandable--label {:data-font-size   :s
                                       :data-font-weight :medium
                                       :data-line-height :text-block}
                                      (x.components/content label)]))

(defn- expandable-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  [:button.e-expandable--header (expandable.helpers/expandable-header-attributes expandable-id expandable-props)
                                [expandable-icon                                 expandable-id expandable-props]
                                [expandable-label                                expandable-id expandable-props]
                                [expandable-expand-button                        expandable-id expandable-props]])

(defn- expandable-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:content (metamorphic-content)(opt)}
  [expandable-id {:keys [content] :as expandable-props}]
  (if (expandable.helpers/expanded? expandable-id)
      [:div.e-expandable--body (expandable.helpers/expandable-body-attributes expandable-id expandable-props)
                               [x.components/content                          expandable-id content]]))

(defn expandable
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  [:div.e-expandable (expandable.helpers/expandable-attributes expandable-id expandable-props)
                     [expandable-header                        expandable-id expandable-props]
                     [expandable-body                          expandable-id expandable-props]])

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
