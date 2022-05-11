
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.popup-b.views
    (:require [layouts.popup-b.prototypes :as prototypes]
              [x.app-components.api       :as components]
              [x.app-core.api             :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-structure
  [popup-id {:keys [content]}]
  [:div.popup-b--content [components/content popup-id content]])

(defn popup-b
  [popup-id {:keys [close-by-cover?] :as layout-props}]
  [:div.popup-b [:div.popup-b--cover (if close-by-cover? {:on-click #(a/dispatch [:ui/close-popup! popup-id])})]
                [layout-structure popup-id layout-props]])

(defn layout
  [popup-id layout-props]
  (let [layout-props (prototypes/layout-props-prototype layout-props)]
       [popup-b popup-id layout-props]))
