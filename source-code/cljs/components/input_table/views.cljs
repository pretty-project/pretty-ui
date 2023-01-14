
(ns components.input-table.views
    (:require [components.input-table.helpers    :as input-table.helpers]
              [components.input-table.prototypes :as input-table.prototypes]
              [elements.api                      :as elements]
              [random.api                        :as random]
              [x.components.api                  :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- input-table-content-block
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (metamorphic-content) row-block
  [table-id _ row-block]
  [:label.c-input-table--content-block [x.components/content table-id row-block]])

(defn- input-table-input-block
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:input-params (vector)(opt)}
  ; @param (vector) row-block
  ; [(metamorphic-content) label
  ;  (keyword) input-id
  ;  (metamorphic-content) input]
  [table-id {:keys [input-params] :as table-props} [input-label input-id input :as row-block]]
  [:<> [:label.c-input-table--input-label (input-table.helpers/row-block-label-attributes table-id table-props row-block)
                                          [x.components/content input-id {:content input-label}]]
       [:div.c-input-table--input-body    [x.components/content input-id {:content input :params input-params}]]])

(defn- input-table-rows
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:rows (vectors in vector)}
  [table-id {:keys [rows] :as table-props}]
  (letfn [(f0 [row-blocks row-block] (conj row-blocks (if (vector? row-block)
                                                          [input-table-input-block   table-id table-props row-block]
                                                          [input-table-content-block table-id table-props row-block])))
          (f1 [rows [row-template & row-blocks]]
              (conj rows [:div.c-input-table--row {:style {:grid-template-columns row-template}}
                                                  (reduce f0 [:<>] row-blocks)]))]
         (reduce f1 [:<>] rows)))

(defn- input-table-label
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:label (metamorphic-content)(opt)}
  [table-id {:keys [label] :as table-props}]
  (if label [:div.c-input-table--label (input-table.helpers/table-label-attributes table-id table-props)
                                       (x.components/content label)]))

(defn- input-table
  ; @param (keyword) table-id
  ; @param (map) table-props
  [table-id table-props]
  [:div.c-input-table (input-table.helpers/table-attributes table-id table-props)
                      [input-table-label table-id table-props]
                      [:div.c-input-table--body (input-table.helpers/table-body-attributes table-id table-props)
                                                [input-table-rows table-id table-props]]])

(defn component
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ; {:border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :border-position (keyword)(opt)
  ;   :all, :bottom, :top, :left, :right, :horizontal, :vertical
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;  :input-params (vector)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;  :rows (vectors in vector)
  ;   [[(string) row-template
  ;     (list of metamorphic-contents or vectors) row-blocks
  ;      [(metamorphic-content) input-label
  ;       (keyword) input-id
  ;       (metamorphic-content) input]]]
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [input-table {...}]
  ;
  ; @usage
  ; [input-table :my-input-table {...}]
  ;
  ; @usage
  ; (defn my-name-field  [] [text-field ::my-name-field  {...}])
  ; (defn my-color-field [] [text-field ::my-color-field {...}])
  ; (defn my-age-field   [] [text-field ::my-age-field   {...}])
  ; [input-table {:rows [["160px 1fr 160px 1fr" [:name  ::my-name-field  #'my-name-field]
  ;                                             [:color ::my-color-field #'my-color-field]]
  ;                      ["160px 1fr"           [:age   ::my-age-field   #'my-age-field]]]}]
  ;
  ; @usage
  ; (defn my-name-field [my-param] [text-field ::my-name-field  {...}])
  ; [input-table {:input-params ["My param"]
  ;               :rows [["160px 1fr" [:name ::my-name-field #'my-name-field]]]}]
  ([table-props]
   [component (random/generate-keyword) table-props])

  ([table-id table-props]
   (let [table-props (input-table.prototypes/table-props-prototype table-props)]
        [input-table table-id table-props])))
