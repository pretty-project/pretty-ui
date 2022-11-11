
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.docs.helpers
    (:require [candy.api         :refer [param return]]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-content->namespace
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "(ns " {})
                   (string/before-first-occurence "\n"   {})
                   (string/not-ends-with!         ")")))

(defn file-content->author
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "; Author: " {})
                   (string/before-first-occurence "\n"         {})))

(defn file-content->created
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "; Created: " {})
                   (string/before-first-occurence "\n"          {})))

(defn file-content->description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "; Description: ")
                   (string/before-first-occurence "\n")))

(defn file-content->version
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "; Version: " {})
                   (string/before-first-occurence "\n"          {})))

(defn file-content->compatibility
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "; Compatibility: " {})
                   (string/before-first-occurence "\n"                {})))

(defn file-content->functions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (second (loop [x [file-content nil]]
                (if (string/contains-part? (first x) "(defn")
                    (let [function-name (-> (first x) (string/after-first-occurence  "(defn" {})
                                                      (string/after-first-occurence  " "     {})
                                                      (string/before-first-occurence "\n"    {}))
                          rest (str "(" (-> (first x) (string/after-first-occurence "(defn"  {})
                                                      (string/after-first-occurence "\n("    {})))]
                         (recur [rest (vector/conj-item (second x) function-name)]))
                    (return x)))))

(defn file-content->docs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  {:namespace     (file-content->namespace     file-content)
   :author        (file-content->author        file-content)
   :created       (file-content->created       file-content)
   :description   (file-content->description   file-content)
   :version       (file-content->version       file-content)
   :compatibility (file-content->compatibility file-content)
   :functions     (file-content->functions     file-content)})
