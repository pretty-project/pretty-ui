
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.popup-a.views
    (:require [layouts.popup-a.helpers    :as helpers]
              [layouts.popup-a.prototypes :as prototypes]
              [react.api                  :as react]
              [reagent.api                :as reagent]
              [x.app-components.api       :as components]
              [x.app-core.api             :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-structure
  [popup-id {:keys [footer]}]
  [:div.popup-a--footer {:data-shadow-visible @layouts.popup-a.state/FOOTER-SHADOW-VISIBLE?}
                        [:div.popup-a--footer-content [components/content popup-id footer]]])

(defn footer
  [popup-id {:keys [footer] :as layout-props}]
  (if footer (reagent/lifecycles {:component-did-mount    (fn [] (helpers/footer-did-mount-f    popup-id))
                                  :component-will-unmount (fn [] (helpers/footer-will-unmount-f popup-id))
                                  :reagent-render         (fn [] [footer-structure popup-id layout-props])})))

(defn body
  [popup-id {:keys [body header]}]
  [:div.popup-a--body (if header [:div {:id (a/dom-value popup-id "header-sensor")}])
                      [:div.popup-a--body-content [components/content popup-id body]]
                      (if footer [:div {:id (a/dom-value popup-id "footer-sensor")}])])


(defn header-structure
  [popup-id {:keys [header]}]
  [:div.popup-a--header {:data-shadow-visible @layouts.popup-a.state/HEADER-SHADOW-VISIBLE?}
                        [:div.popup-a--header-content [components/content popup-id header]]])

(defn header
  [popup-id {:keys [header] :as layout-props}]
  (if header (reagent/lifecycles {:component-did-mount    (fn [] (helpers/header-did-mount-f    popup-id))
                                  :component-will-unmount (fn [] (helpers/header-will-unmount-f popup-id))
                                  :reagent-render         (fn [] [header-structure popup-id layout-props])})))

(defn layout-structure
  [popup-id layout-props]
  [:div.popup-a--layout-structure [:div.popup-a--layout-hack [body   popup-id layout-props]
                                                             [header popup-id layout-props]]
                                  [footer popup-id layout-props]])

(defn popup-a
  [popup-id {:keys [close-by-cover?] :as layout-props}]
  [:div.popup-a (helpers/layout-attributes popup-id layout-props)
                [:div.popup-a--cover (if close-by-cover? {:on-click #(a/dispatch [:ui/close-popup! popup-id])})]
                [layout-structure popup-id layout-props]])

(defn layout
  [popup-id layout-props]
  (let [layout-props (prototypes/layout-props-prototype layout-props)]
       [popup-a popup-id layout-props]))
