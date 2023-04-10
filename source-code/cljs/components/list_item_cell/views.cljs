
(ns components.list-item-cell.views
    (:require [components.list-item-cell.helpers    :as list-item-cell.helpers]
              [components.list-item-cell.prototypes :as list-item-cell.prototypes]
              [css.api                              :as css]
              [elements.api                         :as elements]
              [noop.api                             :refer [return]]
              [random.api                           :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-cell-body
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ; {:rows (maps in vector)}
  [cell-id {:keys [rows] :as cell-props}]
  (letfn [(f [rows row-props]
             (if row-props (conj   rows [elements/label (list-item-cell.prototypes/row-props-prototype row-props)])
                           (return rows)))]
         (reduce f [:<>] rows)))

(defn- list-item-cell
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ; {:width (px)(opt)}
  [cell-id {:keys [width] :as cell-props}]
  [:div.c-list-item-cell (list-item-cell.helpers/cell-attributes cell-id cell-props)
                         [list-item-cell-body cell-id cell-props]])

(defn component
  ; @param (keyword)(opt) cell-id
  ; @param (map) cell-props
  ; {:on-click (Re-Frame metamorphic-event)(opt)
  ;  :rows (maps in vector)
  ;   [{:color (keyword or string)(opt)
  ;      Default: :default
  ;     :content (metamorphic-content)
  ;     :font-size (keyword)(opt)
  ;       Default: :s
  ;     :font-weight (keyword)(opt)
  ;     :placeholder (metamorphic-content)(opt)}]}
  ;
  ; @usage
  ; [list-item-cell {...}]
  ;
  ; @usage
  ; [list-item-cell :my-cell {...}]
  ;
  ; @usage
  ; [list-item-cell :my-cell {:rows [{:content "Row #1"}]}]
  ([cell-props]
   [component (random/generate-keyword) cell-props])

  ([cell-id cell-props]
   (let [] ; cell-props (list-item-cell.prototypes/cell-props-prototype cell-props)
        [list-item-cell cell-id cell-props])))
