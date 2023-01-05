
(ns tools.clipboard.sample
    (:require [re-frame.api        :as r]
              [tools.clipboard.api :as clipboard]))

;; -- Copying text to clipboard -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn copy-my-text-to-clipboard!
  []
  (clipboard/copy-text! "My text"))

;; -- Copying text to clipboard with side-effect event ------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :copy-my-text-to-clipboard!
  {:fx [:clipboard/copy-text! "My text"]})

;; -- Copying text to clipboard (with dialog box) -----------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :copy-my-text-to-clipboard!
  [:clipboard/copy-text! "My text"])
