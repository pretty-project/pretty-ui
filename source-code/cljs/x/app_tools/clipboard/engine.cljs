
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.clipboard.engine
    (:require [app-fruits.dom :as dom]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copy-to-clipboard-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [clipboard (dom/get-element-by-id "x-app-clipboard")]
       (-> clipboard .-value js/navigator.clipboard.writeText)))
