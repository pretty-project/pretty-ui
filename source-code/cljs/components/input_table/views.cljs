
(ns components.input-table.views
    (:require [components.input-table.helpers    :as input-table.helpers]
              [components.input-table.prototypes :as input-table.prototypes]
              [elements.api                      :as elements]
              [random.api                        :as random]
              [x.components.api                  :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- input-table-input
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (vector) input-props
  ; [(metamorphic-content) label
  ;  (keyword) input-id
  ;  (metamorphic-content) input]
  [table-id table-props [label input-id input :as input-props]]
  [:<> [:label.c-input-table--input-label (input-table.helpers/input-label-attributes table-id table-props input-props)
                                          [x.components/content input-id label]]
       [:div.c-input-table--input-body    [x.components/content input-id input]]])

(defn- input-table-rows
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {}
  [table-id {:keys [rows] :as table-props}]
  (letfn [(f0 [inputs input] (conj inputs [input-table-input table-id table-props input]))
          (f1 [rows [template & inputs]]
              (conj rows [:div.c-input-table--row {:style {:grid-template-columns template}}
                                                  (reduce f0 [:<>] inputs)]))]
         (reduce f1 [:<>] rows)))

(defn- input-table
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:border (keyword)(opt)}
  [table-id {:keys [border] :as table-props}]
  [:div.c-input-table [:div.c-input-table--body {:data-border border}
                                                [input-table-rows table-id table-props]]])

(defn component
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ; {:border (keyword)(opt)
  ;   :both, :bottom, :top
  ;  :rows (vectors in vectors in vector)
  ;   [[(string) template
  ;     [(metamorphic-content) label
  ;      (keyword) input-id
  ;      (metamorphic-content) input]]]}
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
  ([table-props]
   [component (random/generate-keyword) table-props])

  ([table-id table-props]
   (let [] ; table-props (input-table.prototypes/table-props-prototype table-props)
        [input-table table-id table-props])))
