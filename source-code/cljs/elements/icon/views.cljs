
(ns elements.icon.views
    (:require [elements.icon.helpers    :as icon.helpers]
              [elements.icon.prototypes :as icon.prototypes]
              [random.api               :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {:icon (keyword)}
  [icon-id {:keys [icon] :as icon-props}]
  [:div.e-icon (icon.helpers/icon-attributes icon-id icon-props)
               [:i.e-icon--body (icon.helpers/icon-body-attributes icon-id icon-props)
                                icon]])

(defn element
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :icon (keyword)
  ;  :icon-color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :icon-family (keyword)(opt)
  ;   :material-symbols-filled, :material-symbols-outlined
  ;   Default: :material-symbols-outlined
  ;  :icon-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :m
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [icon {...}]
  ;
  ; @usage
  ; [icon :my-icon {...}]
  ([icon-props]
   [element (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   (let [icon-props (icon.prototypes/icon-props-prototype icon-props)]
        [icon icon-id icon-props])))
