
(ns templates.item-lister.header.views
    (:require [candy.api               :refer [return]]
              [components.api          :as components]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [logic.api               :refer [nor]]
              [re-frame.api            :as r]
              [vector.api              :as vector]
              [x.components.api        :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- search-field
  ; @param (keyword) lister-id
  ; @param (map) bar-props
  ; {:on-search (metamorphic-event)
  ;  :search-placeholder (metamorphic-content)}
  [lister-id {:keys [on-search search-placeholder]}]
  [elements/search-field ::search-field
                         {;:autofocus?   true
                          :border-color  :highlight
                          :border-radius :l
                          :font-size     :s
                          :on-empty       on-search
                          :on-type-ended  on-search
                          :placeholder    search-placeholder}])

(defn- search-description
  ; @param (keyword) lister-id
  ; @param (map) bar-props
  [lister-id _]
  (let [search-term    @(r/subscribe [:item-lister/get-meta-item      lister-id :search-term])
        all-item-count @(r/subscribe [:item-lister/get-all-item-count lister-id])
        description     (x.components/content {:content :search-results-n :replacements [all-item-count]})]
       [components/section-description {:content (if-not (empty? search-term) description)
                                        :horizontal-align :right}]))

(defn search-bar
  ; @param (keyword) lister-id
  ; @param (map) bar-props
  ; {:on-search (metamorphic-event)
  ;  :placeholder (metamorphic-content)}
  ;
  ; @usage
  ; [search-bar :my-lister {...}]
  [lister-id bar-props]
  [:div#t-item-lister--search-bar [search-field lister-id bar-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compact-list-header
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;
  ; @usage
  ; [compact-list-header :my-lister {...}]
  [lister-id header-props]
  [components/compact-list-header ::compact-list-header
                                  (assoc header-props :border-color    :highlight
                                                      :border-position :bottom
                                                      :fill-color      :default
                                                      :indent          {:horizontal :xxs})])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- breadcrumbs
  ; @param (keyword) lister-id
  ; @param (map) bar-props
  ; {:crumbs (maps in vector)}
  [lister-id {:keys [crumbs]}]
  [elements/breadcrumbs ::breadcrumbs
                        {:crumbs crumbs}])

(defn- title
  ; @param (keyword) lister-id
  ; @param (map) bar-props
  ; {:placeholder (metamorphic-content)(opt)
  ;  :title (metamorphic-content)(opt)}
  [lister-id {:keys [placeholder title]}]
  [components/section-title ::title
                            {:content     title
                             :placeholder placeholder}])

(defn label-bar
  ; @param (keyword) lister-id
  ; @param (map) bar-props
  ; {:crumbs (maps in vector)
  ;   [{:label (metamorphic-content)(opt)
  ;     :placeholder (metamorphic-content)
  ;     :route (string)(opt)}]
  ;  :placeholder (metamorphic-content)(opt)
  ;  :title (metamorphic-content)(opt)}
  ;
  ; @usage
  ; [label-bar :my-lister {...}]
  [lister-id bar-props]
  [:div#t-item-lister--label-bar [:div.t-item-lister--label-bar--block [title       lister-id bar-props]
                                                                       [breadcrumbs lister-id bar-props]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-header-structure
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ; {:cells (maps in vector)
  ;   [{:label (metamorphic-content)(opt)
  ;     :order-by-key (keyword)}]
  ;  :template (string)}
  ;
  ; @usage
  ; [list-header :my-lister {...}]
  [lister-id {:keys [cells template]}]
  (let [current-order-by @(r/subscribe [:item-lister/get-current-order-by lister-id])]
       [components/item-list-header ::list-header
                                    {:border   :bottom
                                     :cells    cells
                                     :order-by current-order-by
                                     :template template}]))

(defn list-header
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ; {:cells (maps in vector)
  ;   [{:label (metamorphic-content)(opt)
  ;     :order-by-key (keyword)}]
  ;  :template (string)}
  ;
  ; @usage
  ; [list-header :my-lister {...}]
  [lister-id {:keys [cells] :as header-props}]
  (letfn [(f0 [{:keys [order-by-key] :as cell}]
              (if order-by-key (assoc  cell :on-click [:item-lister/order-items! lister-id order-by-key])
                               (return cell)))
          (f1 [cells] (vector/->items cells f0))]
         [:div#t-item-lister--list-header [list-header-structure lister-id (update header-props :cells f1)]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! DEPRECATED! DO NOT USE!
(defn header
  [lister-id header-props])
; WARNING! DEPRECATED! DO NOT USE!
