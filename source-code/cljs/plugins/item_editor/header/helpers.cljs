
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.header.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-menu-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) menu-item
  ;  {:change-keys (keywords in vector)(opt)
  ;   :label (metamorphic-content)
  ;   :view-id (keyword)}
  ;
  ; @return (map)
  ;  {:active? (boolean)
  ;   :badge-color (keyword)
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)}
  [editor-id {:keys [change-keys label view-id]}]
  (let [form-changed?    @(a/subscribe [:item-editor/form-changed? editor-id change-keys])
        selected-view-id @(a/subscribe [:item-editor/get-selected-view-id editor-id])]
       {:label       label
        :active?     (= view-id selected-view-id)
        :badge-color (if form-changed? :primary)
        :on-click    [:item-editor/change-view! editor-id view-id]}))

(defn header-menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (maps in vector)
  ;  [{:active? (boolean)
  ;    :badge-color (keyword)
  ;    :label (metamorphic-content)
  ;    :on-click (metamorphic-event)}]
  [editor-id]
  (letfn [(f [menu-items menu-item] (conj menu-items (header-menu-item editor-id menu-item)))]
         (let [menu-items @(a/subscribe [:item-editor/get-header-prop editor-id :menu-items])]
              (reduce f [] menu-items))))
