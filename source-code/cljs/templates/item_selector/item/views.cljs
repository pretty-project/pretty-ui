
(ns templates.item-selector.item.views
    (:require [elements.api :as elements]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- increase-count-button
  ; @param (keyword) selector-id
  ; @param (integer) item-dex
  ; @param (map) counter-props
  ; {:item-id (string)}
  [selector-id _ {:keys [item-id]}]
  (let [item-selected? @(r/subscribe [:item-selector/item-selected? selector-id item-id])
        item-count     @(r/subscribe [:item-selector/get-item-count selector-id item-id])
        disabled?       (or (not item-selected?) (= item-count 255))]
       [elements/icon-button {:icon :add :height :m :width :l
                              :on-click [:item-selector/increase-item-count! selector-id item-id]
                              :disabled? disabled? :color (if disabled? :muted :default)}]))

(defn- decrease-count-button
  ; @param (keyword) selector-id
  ; @param (integer) item-dex
  ; @param (map) counter-props
  ; {:item-id (string)}
  [selector-id _ {:keys [item-id]}]
  (let [item-selected? @(r/subscribe [:item-selector/item-selected? selector-id item-id])
        item-count     @(r/subscribe [:item-selector/get-item-count selector-id item-id])
        disabled?       (or (not item-selected?) (= item-count 1))]
       [elements/icon-button {:icon :remove :height :m :width :l
                              :on-click [:item-selector/decrease-item-count! selector-id item-id]
                              :disabled? disabled? :color (if disabled? :muted :default)}]))

(defn item-counter
  ; @param (keyword) selector-id
  ; @param (integer) item-dex
  ; @param (map) counter-props
  ;
  ; @usage
  ; [item-counter :my-selector 42 {...}]
  [selector-id item-dex counter-props]
  ; BUG#0781 (source-code/app/components/frontend/item_list_table/views.cljs)
  [:div.t-item-selector--item-counter
       [increase-count-button selector-id item-dex counter-props]
       [decrease-count-button selector-id item-dex counter-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-marker
  ; @param (keyword) selector-id
  ; @param (integer) item-dex
  ; @param (map) marker-props
  ; {:disabled? (boolean)(opt)
  ;  :item-id (string)}
  ;
  ; @usage
  ; [item-marker :my-selector 42 {...}]
  [selector-id item-dex {:keys [disabled? item-id]}]
  ; BUG#0781 (source-code/app/components/frontend/item_list_table/views.cljs)
  (let [item-selected? @(r/subscribe [:item-selector/item-selected? selector-id item-id])
        autosaving?    @(r/subscribe [:item-selector/autosaving?    selector-id item-id])]
       [elements/icon-button {:disabled? disabled?
                              :icon (if item-selected? :check_circle_outline :radio_button_unchecked)
                              :on-click [:item-selector/item-clicked selector-id item-id]
                              :progress-duration 1000
                              :progress (if (and item-selected? autosaving?) 100 0)
                              :hover-color :highlight
                              :class       :t-item-selector--cell-button}]))
