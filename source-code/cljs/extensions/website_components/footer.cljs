
(ns extensions.website-components.footer
    (:require [mid-fruits.keyword :as keyword]
              [x.app-core.api     :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-to-top
  ; @param (keyword) footer-id
  ; @param (map) footer-props
  ;
  ; @return (hiccup)
  [_ _]
  [:div#x-website-footer--scroll-to-top
    [:button#x-website-footer--scroll-to-top-icon
      {:on-click #(a/dispatch [:environment/scroll-to-top!])}
      (keyword/to-dom-value :expand_less)]])
