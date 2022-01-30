
(ns x.server-developer.docs
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [server-fruits.http :as http]
              [server-fruits.io   :as io]
              [x.server-core.api  :as a]
              [x.server-user.api  :as user]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def ROOT-DIRECTORY-PATH "source-code")

; @constant (strings in vector)
(def ALLOWED-EXTENSIONS ["clj" "cljc" "cljs"])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-content->namespace
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "(ns ")
                   (string/before-first-occurence "\n")
                   (string/not-ends-with!         ")")))

(defn- file-content->author
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "; Author: ")
                   (string/before-first-occurence "\n")))

(defn- file-content->created
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "; Created: ")
                   (string/before-first-occurence "\n")))

(defn- file-content->description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "; Description: ")
                   (string/before-first-occurence "\n")))

(defn- file-content->version
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "; Version: ")
                   (string/before-first-occurence "\n")))

(defn- file-content->compatibility
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (-> file-content (string/after-first-occurence  "; Compatibility: ")
                   (string/before-first-occurence "\n")))

(defn- file-content->functions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  (second (loop [x [file-content nil]]
                (if (string/contains-part? (first x) "(defn")
                    (let [function-name (-> (first x) (string/after-first-occurence  "(defn")
                                                      (string/after-first-occurence  " ")
                                                      (string/before-first-occurence "\n"))
                          rest (str "(" (-> (first x) (string/after-first-occurence "(defn")
                                                      (string/after-first-occurence "\n(")))]
                         (recur [rest (vector/conj-item (second x) function-name)]))
                    (return x)))))

(defn- file-content->docs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-content]
  {:namespace     (file-content->namespace     file-content)
   :author        (file-content->author        file-content)
   :created       (file-content->created       file-content)
   :description   (file-content->description   file-content)
   :version       (file-content->version       file-content)
   :compatibility (file-content->compatibility file-content)
   :functions     (file-content->functions     file-content)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- read-namespaces
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [directory-path]
  (let [file-list (io/all-file-list directory-path)]
       (reduce #(let [extension (io/filepath->extension %2)]
                     (if (vector/contains-item? ALLOWED-EXTENSIONS extension)
                         (let [file-content (io/read-file %2)]
                              (assoc-in %1 [%2 :docs] (file-content->docs file-content)))
                         (return %1)))
                (param {})
                (param file-list))))

(defn- download-docs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (if (user/request->authenticated? request)
      (http/map-wrap {:body (read-namespaces ROOT-DIRECTORY-PATH)})))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot {:dispatch-n [[:router/add-route! :docs/download-route
                                                     {:route-template "/docs/download-docs"
                                                      :get            #(download-docs %)}]
                                 [:router/add-route! :docs/route
                                                     {:route-template "/@app-home/docs"
                                                      :client-event   [:developer/load-docs!]
                                                      :restricted?    true}]]}})
