
(ns pretty-website.follow-us-links.utils)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn provider->fa-icon-class
  ; @ignore
  ;
  ; @param (keyword) provider
  ;
  ; @usage
  ; (provider->fa-icon-class :instagram)
  ; =>
  ; "fa-instagram"
  ;
  ; @return (string)
  [provider]
  (->> provider (name)
                (str "fa-")
                (keyword)))
