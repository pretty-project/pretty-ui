
(ns templates.item-handler.header.views
    (:require [components.api                        :as components]
              [elements.api                          :as elements]
              [re-frame.api                          :as r]
              [reagent.api                           :as reagent]
              [templates.item-handler.header.helpers :as header.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-item-props
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ; {:label (metamorphic-content)}
  ;
  ; @return (map)
  ; {:active? (boolean)
  ;  :disabled? (boolean)(opt)
  ;  :label (metamorphic-content)
  ;  :on-click (metamorphic-event)}
  [handler-id _ {:keys [disabled? label]}]
  (let [current-view @(r/subscribe [:x.gestures/get-current-view-id handler-id])]
       {:active?   (= label current-view)
        :on-click  [:x.gestures/change-view! handler-id label]
        :disabled? disabled?
        :label     label}))

(defn- menu-bar-structure
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  ; {:menu-items (maps in vector)}
  [handler-id {:keys [menu-items] :as bar-props}]
  (letfn [(f [menu-items menu-item] (conj menu-items (menu-item-props handler-id bar-props menu-item)))]
         [elements/menu-bar ::menu-bar {:menu-items (reduce f [] menu-items)
                                        :line-height :5xl}]))

(defn menu-bar
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  ; {:menu-items (maps in vector)
  ;   [{:disabled? (boolean)(opt)
  ;     :label (metamorphic-content)}]}
  ;
  ; @usage
  ; [menu-bar :my-handler {...}]
  [handler-id bar-props]
  (reagent/lifecycles {:component-did-mount (fn [] (header.helpers/menu-bar-did-mount-f handler-id bar-props))
                       :reagent-render      (fn [_ bar-props] [menu-bar-structure handler-id bar-props])}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- delete-item-button
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  [handler-id bar-props]
  (let [on-failure   [:item-handler/delete-item-failed handler-id bar-props]
        on-success   [:item-handler/item-deleted       handler-id bar-props]
        delete-event [:item-handler/delete-item!       handler-id {:on-failure on-failure :on-success on-success
                                                                   :display-progress? true :progress-max 80
                                                                   :progress-behaviour :keep-faked}]]
       [elements/button ::delete-item-button
                        {:border-radius :xs
                         :color         "#de2050"
                         :font-size     :xs
                         :hover-color   :highlight
                         :icon          :delete_forever
                         :icon-size     :m
                         :indent        {:horizontal :xxs :vertical :s}
                         :icon-color    (if @(r/subscribe [:x.environment/viewport-min? 480]) :default)
                         :label         (if @(r/subscribe [:x.environment/viewport-min? 480]) :delete!)
                         :on-click      delete-event
                         :outdent       {:all :xxs}
                         :tooltip       :permanently-delete-this-item}]))

(defn- duplicate-item-button
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  [handler-id bar-props]
  (let [on-failure      [:item-handler/duplicate-item-failed handler-id bar-props]
        on-success      [:item-handler/item-duplicated       handler-id bar-props]
        duplicate-event [:item-handler/duplicate-item!       handler-id {:on-failure on-failure :on-success on-success
                                                                         :display-progress? true :progress-max 80
                                                                         :progress-behaviour :keep-faked}]]
       [elements/button ::duplicate-item-button
                        {:border-radius :xs
                         :font-size     :xs
                         :hover-color   :highlight
                         :icon          :content_copy
                         :icon-size     :m
                         :indent        {:horizontal :xxs :vertical :s}
                         :label         (if @(r/subscribe [:x.environment/viewport-min? 480]) :duplicate!)
                         :on-click      duplicate-event
                         :outdent       {:all :xxs}
                         :tooltip       :make-a-copy-of-this-item}]))

(defn- revert-item-button
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  [handler-id _]
  (let [item-changed? @(r/subscribe [:item-handler/current-item-changed? handler-id])]
       [elements/button ::revert-item-button
                        {:border-radius :xs
                         :disabled?     (not item-changed?)
                         :font-size     :xs
                         :hover-color   :highlight
                         :icon          :undo
                         :icon-size     :m
                         :indent        {:horizontal :xxs :vertical :s}
                         :label         (if @(r/subscribe [:x.environment/viewport-min? 480]) :revert!)
                         :on-click      [:item-handler/revert-current-item! handler-id]
                         :outdent       {:all :xxs}
                         :tooltip       :discard-changes-of-this-item}]))

(defn- save-item-button
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  [handler-id _]
  (let [item-changed? @(r/subscribe [:item-handler/current-item-changed? handler-id])
        on-failure [:item-handler/save-item-failed handler-id {}]
        on-success [:item-handler/item-saved       handler-id {}]
        save-event [:item-handler/save-item!       handler-id {:on-failure on-failure :on-success on-success
                                                               :display-progress? true}]]
       [elements/button ::save-item-button
                        {:border-radius :xs
                         :color         "#0080fa"
                         :disabled?     (not item-changed?)
                         :font-size     :xs
                         :hover-color   :highlight
                         :icon          :cloud_upload
                         :icon-size     :m
                         :indent        {:horizontal :xxs :vertical :s}
                         :icon-color    (if @(r/subscribe [:x.environment/viewport-min? 480]) :default)
                         :label         (if @(r/subscribe [:x.environment/viewport-min? 480]) :save!)
                         :on-click      save-event
                         :outdent       {:left :s :all :xxs}
                         :tooltip       :save-changes-of-this-item}]))

(defn control-bar
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  ; {:base-route (string)
  ;  :lister-id (keyword)}
  ;
  ; @usage
  ; [control-bar handler-id {...}]
  [handler-id bar-props]
  [:div#t-item-handler--control-bar [save-item-button      handler-id bar-props]
                                    [revert-item-button    handler-id bar-props]
                                    [duplicate-item-button handler-id bar-props]
                                    [delete-item-button    handler-id bar-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- breadcrumbs
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  ; {:crumbs (maps in vector)}
  [_ {:keys [crumbs]}]
  [elements/breadcrumbs ::breadcrumbs
                        {:crumbs crumbs}])

(defn- title
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  ; {:placeholder (metamorphic-content)(opt)
  ;  :title (metamorphic-content)(opt)}
  [_ {:keys [placeholder title]}]
  [components/section-title ::title
                            {:content     title
                             :placeholder placeholder}])

(defn label-bar
  ; @param (keyword) handler-id
  ; @param (map) bar-props
  ; {:crumbs (maps in vector)
  ;   [{:label (metamorphic-content)(opt)
  ;     :placeholder (metamorphic-content)
  ;     :route (string)(opt)}]
  ;  :placeholder (metamorphic-content)(opt)
  ;  :title (metamorphic-content)(opt)}
  ;
  ; @usage
  ; [label-bar :my-handler {...}]
  [handler-id bar-props]
  [:div#t-item-handler--label-bar [:div.t-item-handler--label-bar-block [title       handler-id bar-props]
                                                                        [breadcrumbs handler-id bar-props]]

                                  ; Info items:
                                  ; - Last modified by
                                  ; - Last modified at
                                  ; - Created by
                                  ; - Created at
                                  ;
                                  ; Később:
                                  ; Hol, kivel, hogyan van megosztva
                                  [:div [elements/icon-button ::xxx
                                                              {:icon :info
                                                               :hover-color :highlight
                                                               :border-radius :s
                                                               :tooltip "Item info"
                                                               :tooltip-position :left}]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! DEPRECATED! DO NOT USE!
(defn header
  [handler-id header-props])
; WARNING! DEPRECATED! DO NOT USE!
