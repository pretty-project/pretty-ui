
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.clipboard.side-effects
    (:require [x.app-core.api               :as a]
              [x.app-tools.clipboard.engine :as clipboard.engine]
              [x.app-tools.clipboard.views  :as clipboard.views]
              [x.app-tools.temporary-component.engine :refer [append-temporary-component! remove-temporary-component!]]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copy-to-clipboard!
  ; @usage
  ;  (tools/copy-to-clipboard! "My text")
  ;
  ; @param (string) text
  [text]
  (append-temporary-component! [clipboard.views/clipboard text] clipboard.engine/copy-to-clipboard-f)
  (remove-temporary-component!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:tools/copy-to-clipboard! "My text"]
(a/reg-fx :tools/copy-to-clipboard! copy-to-clipboard!)
