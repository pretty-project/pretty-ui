
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.header.helpers
    (:require [mid-fruits.candy :refer [return]]
              [x.app-core.api   :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-menu-item-badge-color
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) menu-item
  ;  {:change-keys (keywords in vector)(opt)}
  ;
  ; @return (keyword)
  [editor-id {:keys [change-keys]}]
  ; XXX#0455
  ; Mivel "Új elem hozzáadása" módban nem jelenik meg a footer komponensen a "Visszaállítás"
  ; gomb, ezért ilyenkor a header komponensen megjelenő fülek cimkéin sem jelenik meg az
  ; egyes nézetek változását jelző pont (badge).
  (if-not @(a/subscribe [:item-editor/new-item? editor-id])
           (if @(a/subscribe [:item-editor/form-changed? editor-id change-keys])
                (return :primary))))

(defn header-menu-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) menu-item
  ;  {:label (metamorphic-content)
  ;   :view-id (keyword)}
  ;
  ; @return (map)
  ;  {:active? (boolean)
  ;   :badge-color (keyword)
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)}
  [editor-id {:keys [label view-id] :as menu-item}]
  (let [badge-color      (header-menu-item-badge-color editor-id menu-item)
        current-view-id @(a/subscribe [:item-editor/get-current-view-id editor-id])]
       {:active?     (= view-id current-view-id)
        :badge-color badge-color
        :label       label
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
