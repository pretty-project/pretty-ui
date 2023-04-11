
(ns elements.blank.views
    (:require [elements.blank.attributes :as blank.attributes]
              [elements.blank.prototypes :as blank.prototypes]
              [metamorphic-content.api   :as metamorphic-content]
              [random.api                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- blank
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ; {:content (metamorphic-content)}
  [blank-id {:keys [content] :as blank-props}]
  [:div (blank.attributes/blank-attributes blank-id blank-props)
        [:div (blank.attributes/blank-body-attributes blank-id blank-props)
              [metamorphic-content/resolve content]]])

(defn element
  ; @param (keyword)(opt) blank-id
  ; @param (map) blank-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [blank {...}]
  ;
  ; @usage
  ; [blank :my-blank {...}]
  ([blank-props]
   [element (random/generate-keyword) blank-props])

  ([blank-id blank-props]
   (let [] ; blank-props (blank.prototypes/blank-props-prototype blank-props)
        [blank blank-id blank-props])))
