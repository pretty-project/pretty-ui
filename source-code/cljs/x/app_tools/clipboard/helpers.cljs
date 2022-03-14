
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.clipboard.helpers
    (:require [app-fruits.dom :as dom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copy-to-clipboard-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [clipboard (dom/get-element-by-id "x-app-clipboard")]
       (-> clipboard .-value js/navigator.clipboard.writeText)))