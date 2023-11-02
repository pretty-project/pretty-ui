
(ns components.input-table.views
    (:require [components.input-table.helpers    :as input-table.helpers]
              [components.input-table.prototypes :as input-table.prototypes]
              [pretty-elements.api                      :as pretty-elements]
              [metamorphic-content.api           :as metamorphic-content]
              [random.api                        :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- input-table-rows
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:rows (vectors in vector)}
  [table-id {:keys [rows] :as table-props}]
  (letfn [(f0 [row-blocks row-block] (conj row-blocks [metamorphic-content/compose row-block]))
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
                                       (metamorphic-content/compose label)]))

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
   (fn [_ table-props] ; XXX#0106 (README.md#parametering)
       (let [table-props (input-table.prototypes/table-props-prototype table-props)]
            [input-table table-id table-props]))))
