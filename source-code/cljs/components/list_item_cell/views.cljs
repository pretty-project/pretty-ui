
(ns components.list-item-cell.views
    (:require [components.list-item-cell.helpers    :as list-item-cell.helpers]
              [components.list-item-cell.prototypes :as list-item-cell.prototypes]
              [fruits.css.api                       :as css]
              [fruits.random.api                    :as random]
              [pretty-elements.api                  :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-cell-body
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ; {:rows (maps in vector)}
  [cell-id {:keys [rows] :as cell-props}]
  (letfn [(f0 [rows row-props]
              (if row-props (conj rows [pretty-elements/label (list-item-cell.prototypes/row-props-prototype row-props)])
                            (->   rows)))]
         (reduce f0 [:<>] rows)))

(defn- list-item-cell
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ; {:width (px)(opt)}
  [cell-id {:keys [width] :as cell-props}]
  [:div.c-list-item-cell (list-item-cell.helpers/cell-attributes cell-id cell-props)
                         [list-item-cell-body cell-id cell-props]])

(defn view
  ; @param (keyword)(opt) cell-id
  ; @param (map) cell-props
  ; {:on-click (function or Re-Frame metamorphic-event)(opt)
  ;  :rows (maps in vector)
  ;   [{:color (keyword or string)(opt)
  ;      Default: :default
  ;     :content (metamorphic-content)
  ;     :content-placeholder (metamorphic-content)(opt)
  ;     :font-size (keyword, px or string)(opt)
  ;       Default: :s
  ;     :font-weight (keyword or integer)(opt)}]}
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
   [view (random/generate-keyword) cell-props])

  ([cell-id cell-props]
   ; @note (tutorials#parameterizing)
   (fn [_ cell-props]
       (let [] ; cell-props (list-item-cell.prototypes/cell-props-prototype cell-props)
            [list-item-cell cell-id cell-props]))))
