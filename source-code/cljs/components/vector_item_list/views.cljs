
(ns components.vector-item-list.views
    (:require [components.vector-item-list.helpers    :as vector-item-list.helpers]
              [components.vector-item-list.prototypes :as vector-item-list.prototypes]
              [random.api                             :as random]
              [re-frame.api                           :as r]
              [x.components.api                       :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vector-item-list-items
  ; @param (keyword) list-id
  ; @param (map) list-props
  ; {:item-element (symbol)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :value-path (vector)}
  [list-id {:keys [item-element placeholder value-path]}]
  (letfn [(f [items item-dex _] (conj items [item-element item-dex]))]
         (let [items @(r/subscribe [:x.db/get-item value-path])]
              (if (empty? items)
                  [x.components/content list-id placeholder]
                  (reduce-kv f [:<>] items)))))

(defn- vector-item-list
  ; @param (keyword) list-id
  ; @param (map) list-props
  [list-id list-props]
  [:div.c-vector-item-list (vector-item-list.helpers/list-attributes list-id list-props)
                           [:div.c-vector-item-list--body (vector-item-list.helpers/list-body-attributes list-id list-props)
                                                          [vector-item-list-items list-id list-props]]])

(defn component
  ; @param (keyword)(opt) list-id
  ; @param (map) list-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;  :item-element (symbol)
  ;  :outdent (map)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :style (map)(opt)
  ;  :value-path (vector)}
  ;
  ; @usage
  ; [vector-item-list {...}]
  ;
  ; @usage
  ; [vector-item-list :my-vector-item-list {...}]
  ;
  ; @usage
  ; (defn my-item-element [item-dex] ...)
  ; [vector-item-list :my-vector-item-list {:item-element #'my-item-element
  ;                                         :value-path   [:my-items]}]
  ([list-props]
   [component (random/generate-keyword) list-props])

  ([list-id list-props]
   (let [] ; list-props (vector-item-list.prototypes/list-props-prototype list-props)
        [vector-item-list list-id list-props])))
