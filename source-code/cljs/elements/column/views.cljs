
(ns elements.column.views
    (:require [elements.column.helpers    :as column.helpers]
              [elements.column.prototypes :as column.prototypes]
              [random.api                 :as random]
              [x.components.api           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- column-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {:content (metamorphic-content)}
  [column-id {:keys [content] :as column-props}]
  [:div.e-column--body (column.helpers/column-body-attributes column-id column-props)
                       [x.components/content                  column-id content]])

(defn- column
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  [column-id column-props]
  [:div.e-column (column.helpers/column-attributes column-id column-props)
                 [column-body                      column-id column-props]])

(defn element
  ; @param (keyword)(opt) column-id
  ; @param (map) column-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :gap (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :center
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
  ;  :style (map)(opt)
  ;  :stretch-orientation (keyword)(opt)
  ;   :horizontal, :vertical, :both, :none
  ;   Default: :vertical
  ;  :vertical-align (keyword)(opt)
  ;   :top, :center, :bottom, :space-around, :space-between, :space-evenly
  ;   Default: :top
  ;  :wrap-items? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; [elements/column {...}]
  ;
  ; @usage
  ; [elements/column :my-column {...}]
  ([column-props]
   [element (random/generate-keyword) column-props])

  ([column-id column-props]
   (let [column-props (column.prototypes/column-props-prototype column-props)]
        [column column-id column-props])))
